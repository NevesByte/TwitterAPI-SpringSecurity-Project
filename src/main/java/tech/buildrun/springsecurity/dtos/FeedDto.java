package tech.buildrun.springsecurity.dtos;
import java.util.List;

public record FeedDto(List<FeedItemDto> feedItems,
                      int page,
                      int pageSize,
                      int totalPages,
                      int totalElements) {
    
}
