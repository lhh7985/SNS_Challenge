package com.web.curation.feed.like;

import com.web.curation.error.CustomException;
import com.web.curation.error.NotFoundDataException;
import com.web.curation.feed.Feed;
import com.web.curation.feed.FeedDao;
import com.web.curation.member.Member;
import com.web.curation.member.MemberDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.web.curation.error.ErrorCode.*;

@Service
@AllArgsConstructor
public class FeedlikeService {
    FeedlikeDao feedLikeDao;
    FeedDao feedDao;
    MemberDao memberDao;

    @Transactional
    public int likeFeed(FeedlikeDto feedlikeDto) {
        Member member = memberDao.findById(feedlikeDto.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Feed feed = feedDao.findById(feedlikeDto.getFeedId())
                .orElseThrow(() -> new CustomException(FEED_NOT_FOUND));

        feedLikeDao.findFeedlikeByMemberAndFeed(member, feed)
                .ifPresent(feedlike -> {
                    throw new CustomException(FEEDLIKE_DUPLICATE_RESOURCE);
                });

        Feedlike feedlike = Feedlike.builder()
                .member(member)
                .feed(feed)
                .feedLike(feedlikeDto.getFeedLike())
                .build();
        Feedlike savedFeedlike = feedLikeDao.save(feedlike);
        return savedFeedlike.getFeedlikeId();
    }

    public Optional<List<FeedlikeDto>> getfeedlikeList(int feedId) {
        Feed feed = feedDao.findById(feedId)
                .orElseThrow(() -> new CustomException(FEED_NOT_FOUND));

        List<Feedlike> feedlikeList = feedLikeDao.findFeedlikeByFeed(feed).get();
        List<FeedlikeDto> feedlikeDtos = new ArrayList<>();
        for (Feedlike feedlike : feedlikeList) {
            feedlikeDtos.add(FeedlikeDto.builder()
                    .feedlikeId(feedlike.getFeedlikeId())
                    .memberId(feedlike.getMember().getMemberId())
                    .feedId(feedlike.getFeed().getFeedId())
                    .feedLike(feedlike.getFeedLike())
                    .build()
            );
        }

        return Optional.of(feedlikeDtos);
    }

    public boolean updateFeedlike(FeedlikeDto feedlikeDto) {
        Optional<Feedlike> chkFeedlike = feedLikeDao.findById(feedlikeDto.getFeedlikeId());
        if (!chkFeedlike.isPresent()) {
            return false;
        }
        Feedlike feedlike = chkFeedlike.get();
        feedlike.setFeedLike(feedlikeDto.getFeedLike());
        feedLikeDao.save(feedlike);
        return true;
    }

    public boolean deleteFeedlike(FeedlikeDto feedlikeDto) {
        Optional<Feedlike> chkFeedlike = feedLikeDao.findById(feedlikeDto.getFeedlikeId());
        if (!chkFeedlike.isPresent()) {
            return false;
        }
        feedLikeDao.delete(chkFeedlike.get());
        return true;
    }
}