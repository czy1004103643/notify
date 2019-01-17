package xyz.melodyl.mapper.table;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.melodyl.pojo.table.*;
import xyz.melodyl.mapper.table.provider.TableSqlProvider;

import java.util.List;
import java.util.Map;

@Mapper
@Repository("TableMapper")
public interface TableMapper {

    int insertColumnTableMapping(@Param("tableID") Integer tableID,@Param("columnContext") ColumnContext[] columnContexts);

    @Select("SELECT id, table_name, description, command, create_time FROM table_user_mapper " +
            "WHERE user_id = #{userID} AND table_name = #{tableName}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "table_name", property = "tableName"),
            @Result(column = "description", property = "description"),
            @Result(column = "command", property = "command"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "id", property = "columnContexts",
                    many = @Many(select = "xyz.melodyl.mapper.table.TableMapper.selectColumnContext")
            )
    })
    ComplicatedTableContext selectUserTableMapping(@Param("userID") Integer userID, @Param("tableName") String tableName);

    @Select("SELECT column_field_name, column_type, column_length FROM column_table_mapper " +
            "WHERE mapper_id = #{id}")
    @Results({
            @Result(column = "column_field_name", property = "name"),
            @Result(column = "column_type", property = "type"),
            @Result(column = "column_length", property = "length")
    })
    ColumnContext[] selectColumnContext(@Param("id") Integer mapperID);

    @Select("SELECT id, table_name, description, command, create_time FROM table_user_mapper WHERE user_id = #{userID} ORDER BY id DESC LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "table_name", property = "tableName"),
            @Result(column = "description", property = "description"),
            @Result(column = "command", property = "command"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "id", property = "columnContexts",
                    many = @Many(select = "xyz.melodyl.mapper.table.TableMapper.selectColumnContext")
            )
    })
    List<ComplicatedTableContext> selectUserTableMappingList(@Param("userID") Integer userID, @Param("limit")int limit,@Param("offset") int offset);

    @Insert("INSERT INTO table_user_mapper (id, user_id, table_name, description, command, create_time) " +
            "VALUES (#{context.id}, #{userID}, #{context.tableName}, #{context.description}, #{context.command}, #{context.createTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "context.id")
        //是自增id返回到context
    int insertUserTableMapping(@Param("userID") Integer userID, @Param("context") BaseTableContext baseTableContext);

    @Delete("DELETE FROM table_user_mapper WHERE user_id = #{userID} and table_name = #{tableName}")
    int deleteUserTableMapping(@Param("userID") Integer userID, @Param("tableName") String tableName);

    @UpdateProvider(type = TableSqlProvider.class, method = "createTable")
    int createTable(TableSqlBuilder tableSqlBuilder);

    @UpdateProvider(type = TableSqlProvider.class, method = "deleteTable")
    int dropTable(TableSqlBuilder tableSqlBuilder);

    @SelectProvider(type = TableSqlProvider.class, method = "selectTable")
    List<Map<String, String>> selectTableData(TableSqlBuilder tableSqlBuilder, int limit, int offset);

    @InsertProvider(type = TableSqlProvider.class, method = "insertTable")
    int insertTableData(String tableName, TableData tableData);
}
