package com.baidu.fsm.storage.db;

import com.baidu.fsm.storage.StateMachineEntity;
import com.baidu.fsm.storage.StatePersist;
import com.baidu.fsm.storage.StatePersistException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**
 * <pre>
 * {@link SpringDataSourcePersist} will execute storage by {@link org.springframework.jdbc.core.JdbcTemplate} in spring transaction, the transaction is located by <tt>dataSource</tt>,
 * and,if there are no bound transactions the {@link org.springframework.jdbc.core.JdbcTemplate} will get a new Connection from data source.
 * </pre>
 */
public class SpringDataSourcePersist implements StatePersist {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        if (dataSource != null) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
    }

    @Override
    public void createStateMachineInstance(final StateMachineEntity entity) {

        if (jdbcTemplate == null) {
            throw new IllegalArgumentException("Please set dataSource.");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = con.prepareStatement(Statements.FSM_CREATE_FSM, Statement.RETURN_GENERATED_KEYS);
                    entity.setCreated(new Timestamp(System.currentTimeMillis()));
                    entity.setModified(entity.getCreated());

                    statement.setLong(1, entity.getParentId());
                    statement.setString(2, entity.getState());
                    statement.setInt(3, entity.getVersion());
                    statement.setInt(4, entity.getStateDefVersion());
                    statement.setInt(5, entity.getStatus());
                    statement.setTimestamp(6, entity.getCreated());
                    statement.setTimestamp(7, entity.getModified());
                    return statement;
                }
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new StatePersistException("Failed to create state machine instance.", e);
        }
        long id = keyHolder.getKey().longValue();
        entity.setId(id);
    }

    @Override
    public StateMachineEntity findStateMachineInstance(long id) {
        if (jdbcTemplate == null) {
            throw new IllegalArgumentException("Please set dataSource.");
        }

        try {
            return jdbcTemplate.query(Statements.FSM_LOAD_FSM_BY_ID, new Object[]{id}, new ResultSetExtractor<StateMachineEntity>() {
                @Override
                public StateMachineEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
                    StateMachineEntity entity = null;
                    if (rs.next()) {
                        entity = new StateMachineEntity();
                        entity.setId(rs.getLong(1));
                        entity.setParentId(rs.getLong(2));
                        entity.setState(rs.getString(3));
                        InputStream in = rs.getBinaryStream(4);
                        entity.setStateDefVersion(rs.getInt(5));
                        entity.setVersion(rs.getInt(6));
                        entity.setStatus(rs.getInt(7));
                        entity.setCreated(rs.getTimestamp(8));
                        entity.setModified(rs.getTimestamp(9));
                        if (in != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] buffer = new byte[128];

                            try {
                                int count;
                                do {
                                    count = in.read(buffer);
                                    if (count > 0) {
                                        out.write(buffer, 0, count);
                                    }
                                } while (count > 0);
                            } catch (IOException e) {
                                throw new SQLException(e);
                            }
                            entity.setTransitions(out.toByteArray());
                        }
                    }
                    return entity;
                }
            });
        } catch (DataAccessException e) {
            throw new StatePersistException("Failed to query state machine by id", e);
        }
    }

    @Override
    public int updateTransition(final StateMachineEntity entity) {
        final int oldVersion = entity.getVersion();
        entity.setVersion(oldVersion + 1);
        try {
            return jdbcTemplate.update(Statements.FSM_UPDATE_STATE_TRANSMIT, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, entity.getState());
                    ps.setBinaryStream(2, new ByteArrayInputStream(entity.getTransitions()), entity.getTransitions().length);
                    ps.setInt(3, entity.getVersion());
                    ps.setTimestamp(4, entity.getModified());
                    ps.setLong(5, entity.getId());
                    ps.setInt(6, oldVersion);
                }
            });
        } catch (DataAccessException e) {
            throw new StatePersistException("Failed to update transition.", e);
        }
    }

    @Override
    public int removeStateMachine(final long id, final int version) {
        try {
            return jdbcTemplate.update(Statements.FSM_DELETE_STATE_MACHINE, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setLong(1, id);
                    ps.setInt(2, version);
                }
            });
        } catch (DataAccessException e) {
            throw new StatePersistException("Failed to delete state machine, id:" + id, e);
        }
    }

    @Override
    public int updateVariables(StateMachineEntity entity) {
        return 0;
    }

    @Override
    public int saveSubMachine(StateMachineEntity entity) {
        return 0;
    }
}
