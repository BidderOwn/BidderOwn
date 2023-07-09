package site.bidderown.server.bounded_context.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import site.bidderown.server.bounded_context.item.repository.dto.BulkInsertItem;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemJdbcRepository {

    private final JdbcTemplate jdbcTemplate;


    public void insertItemList(List<BulkInsertItem> itemList){
        jdbcTemplate.batchUpdate("insert into item (title,created_at, updated_at, expire_at ,item_status, description , minimum_Price, member_id, bid_count, comment_count) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, itemList.get(i).getTitle());
                        ps.setTimestamp(2, java.sql.Timestamp.valueOf(itemList.get(i).getCreatedDate()));
                        ps.setTimestamp(3, java.sql.Timestamp.valueOf(itemList.get(i).getUpdatedDate()));
                        ps.setTimestamp(4, java.sql.Timestamp.valueOf(itemList.get(i).getExpiredDate()));
                        ps.setString(5, itemList.get(i).getItemStatus());
                        ps.setString(6, itemList.get(i).getDescription());
                        ps.setLong(7, itemList.get(i).getMinimumPrice());
                        ps.setLong(8, itemList.get(i).getMemberId());
                        ps.setLong(9, 0L);
                        ps.setLong(10, 0L);
                    }

                    @Override
                    public int getBatchSize() {
                        return itemList.size();
                    }
                });
    }

}
