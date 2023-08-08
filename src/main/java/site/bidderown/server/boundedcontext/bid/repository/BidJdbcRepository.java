package site.bidderown.server.boundedcontext.bid.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import site.bidderown.server.boundedcontext.bid.controller.dto.BulkInsertBid;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class BidJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void insertBidList(List<BulkInsertBid> bidList){
        jdbcTemplate.batchUpdate("insert into bid (created_at, updated_at, bid_result, bidder_id, item_id, price) values ( ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setTimestamp(1, java.sql.Timestamp.valueOf(bidList.get(i).getCreatedDate()));
                        ps.setTimestamp(2, java.sql.Timestamp.valueOf(bidList.get(i).getUpdatedDate()));
                        ps.setString(3, bidList.get(i).getBidResult());
                        ps.setLong(4, bidList.get(i).getBidderId());
                        ps.setLong(5, bidList.get(i).getItemId());
                        ps.setLong(6, bidList.get(i).getPrice());
                    }

                    @Override
                    public int getBatchSize() {
                        return bidList.size();
                    }
                });
    }
}