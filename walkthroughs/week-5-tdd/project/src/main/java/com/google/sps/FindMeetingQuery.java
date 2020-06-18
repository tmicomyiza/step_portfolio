// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Stores all possible meeting time slots.
    ArrayList<TimeRange> results = new ArrayList<>();

    // Duration of request should be not greater than whole day.
    // Otherwise, there is no possible option.
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return results;
    }

    // No attendees available.
    if (request.getAttendees().isEmpty()) {
      results.add(TimeRange.WHOLE_DAY);
      return results;
    }
    
    // No conflict because no other event is scheduled.
    if (events.isEmpty()) {
      results.add(TimeRange.WHOLE_DAY);
      return results;
    }

    // Find all occupied time by other events.
    ArrayList<TimeRange> allOccupiedTime = occupiedTimes(events, request);


    return results = allFreeTime(mergeOverlappingRanges(allOccupiedTime), request);
  }


  /**
   * Evaluates whether there's an attendee who will attend an existing event and 
   *  will attended the requested meeting.
  */
  private static boolean attendanceConflict (Event existingEvent, MeetingRequest request) {
    Set<String> existingEventAttendees = new HashSet<>(existingEvent.getAttendees());

    // Evaluates whether there's an intersection between the sets of attendees.
    existingEventAttendees.retainAll(request.getAttendees());

    return !existingEventAttendees.isEmpty();
  }

  /**
   * Connect all time that overlap.
   *  
  */
  private static ArrayList<TimeRange> mergeOverlappingRanges (ArrayList<TimeRange> timeRanges)  {
    // Sort the time ranges by start time.
    Collections.sort(timeRanges, TimeRange.ORDER_BY_START);
    
    ArrayList<TimeRange> sortedTimeRanges = new ArrayList<>();
    //int length = sortedTimeRanges.size();

    for (TimeRange currentTime : timeRanges){
      // No overlap or the its the first element we are evaluating.
      if (sortedTimeRanges.isEmpty() || !currentTime.overlaps(sortedTimeRanges.get(sortedTimeRanges.size() - 1))) {
        sortedTimeRanges.add(currentTime);

      } else {
        // Create a new range that contains both ranges.
        TimeRange lastTime = sortedTimeRanges.get(sortedTimeRanges.size() - 1);
        int start = Math.min(lastTime.start(), currentTime.start());
        int end = Math.max (lastTime.end(), currentTime.end());

        TimeRange newTimeRange = TimeRange.fromStartEnd(start, end, false);

        sortedTimeRanges.remove(lastTime);
        sortedTimeRanges.add(newTimeRange);
      }
    }

    return sortedTimeRanges;
  }

  /**
   * Retrieve all occupied time by other events.
   * 
  */
  private static ArrayList<TimeRange> occupiedTimes (Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> currentOccupiedSpots = new ArrayList<>();

    for (Event event : events){
      // In case event has no attendees conflict with requested meeting,
      // Then it's timeRange is considered unoccupied.
      if (attendanceConflict(event, request)) {
        currentOccupiedSpots.add(event.getWhen());
      }
    }

    return currentOccupiedSpots;
  }

  /**
   * Retrieve all available time given the list of time occupied by other events.
   *  
  */
  private static ArrayList<TimeRange> allFreeTime (ArrayList<TimeRange> allOccupiedTime, MeetingRequest request) {
    ArrayList<TimeRange> freeTimes = new ArrayList<>();
    TimeRange possibleTime;
    int prevEndTime = TimeRange.START_OF_DAY;

    for (TimeRange currentTime : allOccupiedTime) {
      possibleTime = TimeRange.fromStartEnd(prevEndTime, currentTime.start(), false);

      if (possibleTime.duration() >= request.getDuration()) {
        freeTimes.add(possibleTime);
      }
      prevEndTime = currentTime.end();
    }

    // Evaluate whether there's an open time after last occupied time.
    possibleTime = TimeRange.fromStartEnd(prevEndTime, TimeRange.END_OF_DAY, true);
    if (possibleTime.duration() >= request.getDuration()) {
      freeTimes.add(possibleTime);
    }

    return freeTimes;
  }

}