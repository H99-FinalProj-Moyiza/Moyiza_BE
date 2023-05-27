package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.entity.EventAttendant;
import lombok.Getter;

import java.util.Calendar;
import java.util.List;

@Getter
public class EventDetailResponseDto {
    private long id;
    private String eventTitle;
    private String eventContent;
    private String eventLocation;
//    private Tag tag;
    private Calendar eventStartTime;
    private int eventGroupSize;
    private boolean deleted;
//    private String image;
    private List<EventAttendant> eventAttendantList;
    private int eventAttendantListSize;
    public EventDetailResponseDto(Event event, List<EventAttendant> attendantList, int people) {
        this.eventTitle = event.getEventTitle();
        this.eventContent = event.getEventContent();
        this.eventLocation = event.getEventLocation();
        this.eventStartTime = event.getEventStartTime();
        this.eventGroupSize = event.getEventGroupSize();
        this.deleted = event.isDeleted();
        this.eventAttendantList = attendantList;
        this.eventAttendantListSize = people;
    }
//
//    public PostResponseDto(Post post) {
//        for (ImagePath imagePath: post.getImagePathList()) {
//            ImagePathResponseDto imagePathResponseDto = new ImagePathResponseDto(imagePath);
//            this.imagePathList.add(imagePathResponseDto);
//        }
//
//
//        this.postId = post.getPostId();
//        this.postTitle = post.getPostTitle();
//        this.postContent = post.getPostContent();
//        this.postPrice = post.getPostPrice();
//        this.interestCount = (int) post.getInterests().stream().filter(Interest::getInterestStatus).count();
//        this.tradeLocation = post.getTradeLocation();
//        this.tradeState = post.getTradeState();
//        this.userId = post.getUser().getUserId();
//        this.createdAt = post.getCreatedAt();
//        this.modifiedAt = post.getModifiedAt();
//        this.specificLocation = post.getSpecificLocation();
//        this.isShared = post.getIsShared();
////        this.myinterest = post.getInterests().stream()
////            .anyMatch(interest -> interest.getUser().getUserId().equals(userId) && interest.getInterest_status());
//    }
}
