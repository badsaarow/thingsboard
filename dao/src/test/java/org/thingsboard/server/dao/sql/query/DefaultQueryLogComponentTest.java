
package org.thingsboard.server.dao.sql.query;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.id.TenantId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DefaultQueryLogComponent.class)
class DefaultQueryLogComponentTest {

    private TenantId tenantId;
    private QueryContext ctx;

    @Autowired
    private DefaultQueryLogComponent queryLog;

    @BeforeEach
    void setUp() {
        tenantId = new TenantId(Uuids.timeBased());
        ctx = new QueryContext(new QuerySecurityContext(tenantId, null, EntityType.ALARM));
    }

    @Test
    void substituteParametersInSqlString_StringType() {

        String Name = "Mery";
        String id = "ID_1";
        String sql = "Select * from Table Where name = :name AND id = :id";
        String sqlToUse = "Select * from Table Where name = 'Mery' AND id = 'ID_1'";

        ctx.addStringParameter("name", Name);
        ctx.addStringParameter("id", id);

        String sqlToUse2 = queryLog.substituteParametersInSqlString(sql, ctx);
        assertEquals(sqlToUse, sqlToUse2);
    }

    @Test
    void substituteParametersInSqlString_DoubleLongType() {

        double sum = 10.55;
        long price = 100000;
        String sql = "Select * from Table Where sum = :sum AND price = :price";
        String sqlToUse = "Select * from Table Where sum = 10.55 AND price = 100000";

        ctx.addDoubleParameter("sum", sum);
        ctx.addLongParameter("price", price);

        String sqlToUse2 = queryLog.substituteParametersInSqlString(sql, ctx);
        assertEquals(sqlToUse, sqlToUse2);
    }

    @Test
    void substituteParametersInSqlString_BooleanType() {

        boolean check = true;
        String sql = "Select * from Table Where check = :check AND mark = :mark";
        String sqlToUse = "Select * from Table Where check = true AND mark = false";

        ctx.addBooleanParameter("check", check);
        ctx.addBooleanParameter("mark", false);

        String sqlToUse2 = queryLog.substituteParametersInSqlString(sql, ctx);
        assertEquals(sqlToUse, sqlToUse2);
    }

    @Test
    void substituteParametersInSqlString_UuidType() {

        UUID guid = Uuids.timeBased();
        String sql = "Select * from Table Where guid = :guid";
        String sqlToUse = "Select * from Table Where guid = '" + guid.toString() + "'";

        ctx.addUuidParameter("guid", guid);

        String sqlToUse2 = queryLog.substituteParametersInSqlString(sql, ctx);
        assertEquals(sqlToUse, sqlToUse2);
    }

    @Test
    void substituteParametersInSqlString_StringListType() {

        List<String> ids = new ArrayList<>();
        ids.add("ID_1");
        ids.add("ID_2");
        ids.add("ID_3");
        ids.add("ID_4");

        String sql = "Select * from Table Where id IN (:ids)";
        String sqlToUse = "Select * from Table Where id IN ('ID_1', 'ID_2', 'ID_3', 'ID_4')";

        ctx.addStringListParameter("ids", ids);

        String sqlToUse2 = queryLog.substituteParametersInSqlString(sql, ctx);
        assertEquals(sqlToUse, sqlToUse2);
    }

    @Test
    void substituteParametersInSqlString_UuidListType() {

        List<UUID> guids = new ArrayList<>();
        guids.add(UUID.fromString("634a8d03-6871-4e01-94d0-876bf3e67dff"));
        guids.add(UUID.fromString("3adbb5b8-4dc6-4faf-80dc-681a7b518b5e"));
        guids.add(UUID.fromString("63a50f0c-2058-4d1d-8f15-812eb7f84412"));

        String sql = "Select * from Table Where guid IN (:guids)";
        String sqlToUse = "Select * from Table Where guid IN ('634a8d03-6871-4e01-94d0-876bf3e67dff', '3adbb5b8-4dc6-4faf-80dc-681a7b518b5e', '63a50f0c-2058-4d1d-8f15-812eb7f84412')";

        ctx.addUuidListParameter("guids", guids);

        String sqlToUse2 = queryLog.substituteParametersInSqlString(sql, ctx);
        assertEquals(sqlToUse, sqlToUse2);
    }
}

