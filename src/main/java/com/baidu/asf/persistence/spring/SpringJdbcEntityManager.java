package com.baidu.asf.persistence.spring;

import com.baidu.asf.model.NodeType;
import com.baidu.asf.persistence.ASFPersistenceException;
import com.baidu.asf.persistence.EntityManager;
import com.baidu.asf.persistence.EntityNotFoundException;
import com.baidu.asf.persistence.MVCCException;
import com.baidu.asf.persistence.enitity.*;
import com.baidu.asf.util.ByteArrayObjectLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The implementation of spring jdbc template
 */
public class SpringJdbcEntityManager implements EntityManager {

    private final JdbcTemplate jdbcTemplate;

    public SpringJdbcEntityManager(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Callback interface for setting entity
     */
    interface EntitySetter<T extends Entity> {
        void setEntity(SqlRowSet rs, T entity) throws SQLException;
    }

    /**
     * Callback interface for setting statement
     */
    interface StatementSetter<T extends Entity> {
        void setPreparedStatement(PreparedStatement statement, T entity) throws SQLException;
    }

    /**
     * Instance entity setter
     */
    class ASFInstanceEntitySetter implements EntitySetter<InstanceEntity> {
        @Override
        public void setEntity(SqlRowSet rowSet, InstanceEntity entity) throws SQLException {
            entity.setId(rowSet.getLong(1));
            entity.setDefId(rowSet.getString(2));
            entity.setDefVersion(rowSet.getInt(3));
            entity.setStatus(rowSet.getInt(4));
            entity.setVersion(rowSet.getInt(5));
            entity.setCreated(rowSet.getTimestamp(6));
            entity.setModified(rowSet.getTimestamp(7));
        }
    }

    /**
     * Execution entity setter
     */
    class ExecutionEntitySetter implements EntitySetter<ExecutionEntity> {
        @Override
        public void setEntity(SqlRowSet rowSet, ExecutionEntity entity) throws SQLException {
            entity.setId(rowSet.getInt(1));
            entity.setInstanceId(rowSet.getLong(2));
            entity.setNodeFullId(rowSet.getString(3));
            entity.setNodeType(NodeType.get(rowSet.getInt(4)));
            entity.setCreated(rowSet.getTimestamp(5));
            entity.setModified(rowSet.getTimestamp(6));
        }
    }

    /**
     * Variable entity setter
     */
    class VariableEntitySetter implements EntitySetter<VariableEntity> {
        @Override
        public void setEntity(SqlRowSet rs, VariableEntity entity) throws SQLException {
            entity.setId(rs.getLong(1));
            entity.setInstanceId(rs.getLong(2));
            entity.setName(rs.getString(3));

            VariableEntity.VariableType type = VariableEntity.VariableType.get(rs.getInt(8));
            if (type == VariableEntity.VariableType.LONG) {
                entity.setLong(rs.getLong(5));
            } else if (type == VariableEntity.VariableType.BOOLEAN) {
                entity.setBoolean(rs.getLong(5) == 1L);
            } else if (type == VariableEntity.VariableType.DOUBLE) {
                entity.setDouble(rs.getDouble(4));
            } else if (type == VariableEntity.VariableType.STRING) {
                entity.setString(rs.getString(6));
            } else if (type == VariableEntity.VariableType.OBJECT) {
                Object obj = rs.getObject(7);

                if (obj instanceof byte[]) {
                    obj = new ByteArrayObjectLoader<Object>((byte[]) obj);
                }
                entity.setObject(obj);
            }
            entity.setVariableClass(VariableEntity.VariableClass.get(rs.getInt(9)));
            entity.setVersion(rs.getInt(10));
            entity.setCreated(rs.getTimestamp(11));
            entity.setModified(rs.getTimestamp(12));
        }
    }

    @Override
    public void createASFInstance(final InstanceEntity instanceEntity) {
        createEntity(SQLConstants.ASF_CREATE_INSTANCE, instanceEntity, new StatementSetter<InstanceEntity>() {
            @Override
            public void setPreparedStatement(PreparedStatement statement, InstanceEntity entity) throws SQLException {
                statement.setString(1, entity.getDefId());
                statement.setInt(2, entity.getDefVersion());
                statement.setInt(3, entity.getStatus());
                statement.setInt(4, entity.getVersion());
            }
        });
    }

    /**
     * The common method for creating entity from sql
     */
    private <T extends Entity> void createEntity(final String sql, final T entity, final StatementSetter<T>
            setter) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    entity.setCreated(new Timestamp(System.currentTimeMillis()));
                    entity.setModified(new Timestamp(System.currentTimeMillis()));

                    PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    setter.setPreparedStatement(statement, entity);

                    return statement;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new com.baidu.asf.persistence.DuplicateKeyException(entity, e);
        } catch (DataAccessException e) {
            throw new ASFPersistenceException("Failed to create entity " + entity.getClass().getName(), e);
        }

        long id = keyHolder.getKey().longValue();
        entity.setId(id);
    }

    /**
     * The common method for querying entity from sql
     */
    private <T extends Entity> T queryForEntity(final String sql, final Class<T> entityClass,
                                                final EntitySetter<T> getter, final Object... args) {
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, args);

            if (rowSet.next()) {
                try {
                    T entity = entityClass.newInstance();
                    getter.setEntity(rowSet, entity);
                    return entity;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new EntityNotFoundException(0, "Not found the Entity by :" + Arrays.toString(args));
            }
        } catch (DataAccessException e) {
            throw new ASFPersistenceException("Failed to load instance " + entityClass.getName(), e);
        }
    }

    /**
     * The common method for query entity list from sql
     */
    private <T extends Entity> List<T> queryForEntityList(final String sql, final Class<T> entityClass,
                                                          final EntitySetter<T> entitySetter, final Object... args) {
        final List<T> entities = new ArrayList<T>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, args);

            while (rowSet.next()) {
                try {
                    T entity = entityClass.newInstance();
                    entitySetter.setEntity(rowSet, entity);
                    entities.add(entity);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (DataAccessException e) {
            throw new ASFPersistenceException("Failed to load instance " + entityClass.getName(), e);
        }

        return entities;
    }

    @Override
    public InstanceEntity loadASFInstance(final long id) {
        return queryForEntity(SQLConstants.ASF_LOAD_INSTANCE, InstanceEntity.class, new ASFInstanceEntitySetter(), id);
    }

    @Override
    public List<InstanceEntity> findASFInstances() {
        return queryForEntityList(SQLConstants.ASF_LOAD_ALL_INSTANCES, InstanceEntity.class,
                new ASFInstanceEntitySetter());
    }

    @Override
    public void updateASFInstanceStatus(InstanceEntity instanceEntity) {
        instanceEntity.backupAndIncrementVersion();

        try {
            int count = jdbcTemplate.update(SQLConstants.ASF_UPDATE_INSTANCE_STATUS, instanceEntity.getStatus(),
                    instanceEntity.getVersion(), instanceEntity.getId(), instanceEntity.getOldVersion());

            if (count == 0) {
                throw new MVCCException(instanceEntity, instanceEntity.getOldVersion());
            }
        } catch (DataAccessException e) {
            throw new ASFPersistenceException("Failed to update instance status.", e);
        }
    }

    @Override
    public void createExecution(final ExecutionEntity executionEntity) {
        createEntity(SQLConstants.ASF_CREATE_EXECUTION, executionEntity, new StatementSetter<ExecutionEntity>() {
            @Override
            public void setPreparedStatement(PreparedStatement statement, ExecutionEntity entity) throws SQLException {
                statement.setLong(1, executionEntity.getInstanceId());
                statement.setString(2, executionEntity.getNodeFullId());
                statement.setInt(3, executionEntity.getNodeType().type);
            }
        });
    }

    @Override
    public ExecutionEntity loadExecution(final long id) {
        return queryForEntity(SQLConstants.ASF_LOAD_EXECUTION, ExecutionEntity.class, new ExecutionEntitySetter(), id);
    }

    @Override
    public void removeExecution(long id) {
        try {
            int count = jdbcTemplate.update(SQLConstants.ASF_DELETE_EXECUTION, id);
            if (count == 0) {
                throw new MVCCException(null, 0);
            }
        } catch (DataAccessException e) {
            throw new ASFPersistenceException("Failed to delete execution entity by id:" + id, e);
        }
    }

    @Override
    public void removeExecutions(long instanceId) {
        try {
            int count = jdbcTemplate.update(SQLConstants.ASF_DELETE_EXECUTIONS, instanceId);
            if (count == 0) {
                throw new MVCCException(null, 0);
            }
        } catch (DataAccessException e) {
            throw new ASFPersistenceException("Failed to delete execution entity by instance id:" + instanceId, e);
        }
    }

    @Override
    public List<ExecutionEntity> findExecutions(long instanceId) {
        return queryForEntityList(SQLConstants.ASF_FIND_EXECUTIONS, ExecutionEntity.class,
                new ExecutionEntitySetter(), instanceId);
    }

    @Override
    public void createTransition(final TransitionEntity transitionEntity) {
        createEntity(SQLConstants.ASF_CREATE_TRANSITION, transitionEntity, new StatementSetter<TransitionEntity>() {
            @Override
            public void setPreparedStatement(PreparedStatement statement, TransitionEntity entity) throws SQLException {
                statement.setLong(1, transitionEntity.getInstanceId());
                statement.setString(2, transitionEntity.getSourceRef());
                statement.setString(3, transitionEntity.getTargetRef());
                statement.setInt(4, transitionEntity.isVirtualFlow() ? 1 : 0);
                statement.setInt(5, transitionEntity.getSourceNodeType().type);
                statement.setInt(6, transitionEntity.getTargetNodeType().type);
            }
        });
    }

    @Override
    public List<TransitionEntity> findTransitions(long instanceId) {
        return queryForEntityList(SQLConstants.ASF_FIND_TRANSITIONS, TransitionEntity.class, new EntitySetter<TransitionEntity>() {
            @Override
            public void setEntity(SqlRowSet rowSet, TransitionEntity entity) throws SQLException {
                entity.setId(rowSet.getLong(1));
                entity.setInstanceId(rowSet.getLong(2));
                entity.setSourceRef(rowSet.getString(3));
                entity.setTargetRef(rowSet.getString(4));
                entity.setVirtualFlow(rowSet.getInt(5) == 1);
                entity.setSourceNodeType(NodeType.get(rowSet.getInt(6)));
                entity.setTargetNodeType(NodeType.get(rowSet.getInt(7)));
            }
        }, instanceId);
    }

    @Override
    public void createVariable(VariableEntity variable) {
        createEntity(SQLConstants.getCreateVariableSQL(variable), variable, new StatementSetter<VariableEntity>() {
            @Override
            public void setPreparedStatement(PreparedStatement statement, VariableEntity entity) throws SQLException {
                statement.setLong(1, entity.getInstanceId());
                statement.setString(2, entity.getName());

                if (entity.getType() == VariableEntity.VariableType.DOUBLE) {
                    statement.setDouble(3, entity.getDouble());
                } else if (entity.getType() == VariableEntity.VariableType.LONG || entity.getType() == VariableEntity.VariableType.BOOLEAN) {
                    statement.setLong(3, entity.getLong());
                } else if (entity.getType() == VariableEntity.VariableType.STRING) {
                    statement.setString(3, entity.getString());
                } else {
                    ByteArrayOutputStream bous = new ByteArrayOutputStream();
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(bous);
                        oos.writeObject(entity.getObject());
                    } catch (Exception e) {
                        //ignore
                    }
                    final byte[] b = bous.toByteArray();
                    statement.setBinaryStream(3, new ByteArrayInputStream(b), b.length);
                }

                statement.setInt(4, entity.getType().value);
                statement.setInt(5, entity.getVariableClass().value);
                statement.setInt(6, entity.getVersion());
            }
        });
    }

    @Override
    public VariableEntity loadVariable(long instanceId, String name, VariableEntity.VariableClass variableClass) {
        return queryForEntity(SQLConstants.ASF_LOAD_VARIABLE, VariableEntity.class, new VariableEntitySetter(), instanceId, name,
                variableClass.value);
    }

    @Override
    public VariableEntity findVariable(long instanceId, String name, VariableEntity.VariableClass variableClass) {
        try {
            return loadVariable(instanceId, name, variableClass);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @Override
    public void updateVariable(VariableEntity entity) {
        entity.backupAndIncrementVersion();

        try {
            int count = jdbcTemplate.update(SQLConstants.getUpdateVariableSQL(entity), entity.getValue(),
                    entity.getType().value, entity.getVersion(), entity.getId(), entity.getOldVersion());

            if (count == 0) {
                throw new MVCCException(entity, entity.getOldVersion());
            }
        } catch (DataAccessException e) {
            throw new ASFPersistenceException("Failed to update variable.", e);
        }
    }

    @Override
    public void removeVariable(long instanceId, String name, VariableEntity.VariableClass variableClass) {
        try {
            int count = jdbcTemplate.update(SQLConstants.ASF_DELETE_VARIABLE, instanceId, name, variableClass.value);

            if (count == 0) {
                throw new MVCCException(null, 0);
            }
        } catch (DataAccessException e) {
            throw new ASFPersistenceException("Failed to delete variable.", e);
        }
    }

    @Override
    public void clearVariables(long instanceId, VariableEntity.VariableClass variableClass) {
        try {
            jdbcTemplate.update(SQLConstants.ASF_CLEAR_VARIABLES, instanceId, variableClass.value);
        } catch (DataAccessException e) {
            throw new ASFPersistenceException("Failed to clear variables", e);
        }
    }

    @Override
    public List<VariableEntity> findVariables(long instanceId) {
        return queryForEntityList(SQLConstants.ASF_FIND_VARIABLES, VariableEntity.class, new VariableEntitySetter());
    }
}
