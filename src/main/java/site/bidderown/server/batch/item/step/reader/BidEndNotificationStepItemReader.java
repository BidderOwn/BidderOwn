package site.bidderown.server.batch.item.step.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.data.domain.PageRequest;
import site.bidderown.server.bounded_context.bid.repository.BidCustomRepository;
import site.bidderown.server.bounded_context.bid.repository.dto.BidEndNotification;

import java.util.Iterator;
import java.util.List;

@Slf4j
public class BidEndNotificationStepItemReader implements ItemReader<BidEndNotification> {

    private final BidCustomRepository bidCustomRepository;
    private Iterator<BidEndNotification> bidIterator;
    private int page = 0;
    private final int CHUNK_SIZE;

    public BidEndNotificationStepItemReader(BidCustomRepository bidCustomRepository, int chunkSize) {
        this.bidCustomRepository = bidCustomRepository;
        this.CHUNK_SIZE = chunkSize;
    }

    @Override
    public BidEndNotification read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (bidIterator == null || !bidIterator.hasNext()) {
            List<BidEndNotification> bids = bidCustomRepository.findBidByItemExpireAt(PageRequest.of(page, CHUNK_SIZE));

            if (bids.isEmpty()) return null;

            bidIterator = bids.iterator();
            page++;
        }

        if (bidIterator.hasNext()) {
            return bidIterator.next();
        }

        return null;
    }
}
