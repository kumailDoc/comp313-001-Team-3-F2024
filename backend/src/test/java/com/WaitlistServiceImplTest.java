package com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.queueontario.backend.models.ServiceOntarioCenter;
import com.queueontario.backend.models.User;
import com.queueontario.backend.models.UserWaitList;
import com.queueontario.backend.models.Waitlist;
import com.queueontario.backend.repository.ServiceOntarioCenterRepository;
import com.queueontario.backend.repository.UserRepository;
import com.queueontario.backend.repository.UserWaitlistRepo;
import com.queueontario.backend.repository.WaitlistRepo;
import com.queueontario.backend.service.WaitlistServiceImpl;
import com.queueontario.backend.utils.EmailService;

@ExtendWith(MockitoExtension.class)
public class WaitlistServiceImplTest {

    @InjectMocks
    private WaitlistServiceImpl waitlistServiceImpl;

    @Mock
    private WaitlistRepo waitlistRepo;

    @Mock
    private ServiceOntarioCenterRepository serviceOntarioCenterRepository;

    @Mock
    private UserWaitlistRepo userWaitListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService; 

      /**
     * Test case to add a user to the waitlist with valid inputs.
     * This test verifies that the user is successfully added to the waitlist.
     */
    @Test
    void testAddUserToWaitlist_ValidInputs() {
        // Arrange
        String serviceOntarioCenterId = "6726460aa806e75c196b4402";
        String userId = "672ac4c6f2a5071a8c13bef0";

        // Mock Waitlist
        Waitlist waitlist = new Waitlist();
        waitlist.setWaitlistId("6747ed431b2b466aab4e6479");
        waitlist.setWaitlisters(new ArrayList<>());
        waitlist.setIsActive("true");

        // Mock User
        User user = new User();
        user.setId(userId);
        user.setUsername("John Doe");
        user.setEmail("john.doe@example.com");

        // Mock ServiceOntarioCenter
        ServiceOntarioCenter serviceOntarioCenter = new ServiceOntarioCenter();
        serviceOntarioCenter.setId(serviceOntarioCenterId);
        serviceOntarioCenter.setName("Downtown Toronto Center");

        // Mock UserWaitList
        UserWaitList userWaitList = new UserWaitList();
        userWaitList.setUserId(userId);
        userWaitList.setWaitlistIdList(new ArrayList<>());

        // Mock repository behavior
        when(waitlistRepo.findByLocationId(serviceOntarioCenterId)).thenReturn(Optional.of(waitlist)); 
        when(userRepository.findById(userId)).thenReturn(Optional.of(user)); 
        when(serviceOntarioCenterRepository.findById(serviceOntarioCenterId)).thenReturn(Optional.of(serviceOntarioCenter)); 
        when(userWaitListRepository.findUserByUserId(userId)).thenReturn(Optional.of(userWaitList)); 
        when(waitlistRepo.save(any(Waitlist.class))).thenReturn(waitlist); 
        when(userWaitListRepository.save(any(UserWaitList.class))).thenReturn(userWaitList); 

        // Act
        Waitlist result = waitlistServiceImpl.addUserToWaitlist(serviceOntarioCenterId, userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.getWaitlisters().contains(userId));
    }

    /**
     * Test case for successfully removing a user from the waitlist.
     * This test verifies that the user is removed and repositories are updated.
     */
    @Test
    void testRemoveUserFromWaitlist_Success() {
        // Arrange
        String waitlistId = "6747ed431b2b466aab4e6479";
        String userId = "672ac4c6f2a5071a8c13bef0";

        // Mock Waitlist
        Waitlist waitlist = new Waitlist();
        waitlist.setWaitlistId(waitlistId);
        ArrayList<String> waitlisters = new ArrayList<>();
        waitlisters.add(userId); 
        waitlist.setWaitlisters(waitlisters);

        // Mock UserWaitList
        UserWaitList userWaitList = new UserWaitList();
        userWaitList.setUserId(userId);

        // Mock repository behavior
        when(waitlistRepo.findById(waitlistId)).thenReturn(Optional.of(waitlist)); 
        doNothing().when(userWaitListRepository).deleteByUserId(userId); 
        when(userWaitListRepository.findUserByUserId(userId)).thenReturn(Optional.empty()); 
        // Act
        boolean result = waitlistServiceImpl.removeUserFromWaitlistByWaitlistId(waitlistId, userId);

        // Assert
        assertTrue(result); 
        verify(waitlistRepo, times(1)).save(waitlist); 
        verify(userWaitListRepository, times(1)).deleteByUserId(userId); 
    }


    /**
     * Test case for attempting to remove a user from the waitlist when the user does not exist.
     * This test verifies that the method returns false and no changes are made to the waitlist or user repository.
     */
    @Test
    void testRemoveUserFromWaitlist_UserNotFound() {
        // Arrange
        String waitlistId = "6747ed431b2b466aab4e6479";
        String userId = "672aa61ef1edcc00fe7906db";

        // Mock Waitlist
        Waitlist waitlist = new Waitlist();
        waitlist.setWaitlistId(waitlistId);
        waitlist.setWaitlisters(new ArrayList<>()); 

        // Mock repository behavior
        when(waitlistRepo.findById(waitlistId)).thenReturn(Optional.of(waitlist)); 

        // Act
        boolean result = waitlistServiceImpl.removeUserFromWaitlistByWaitlistId(waitlistId, userId);

        // Assert
        assertFalse(result); 
        verify(waitlistRepo, never()).save(any(Waitlist.class)); 
        verify(userWaitListRepository, never()).deleteByUserId(userId); 
    }


/**
     * Test case to update the average wait time and remove specified users from the waitlist.
     * This test verifies that the waitlist's average wait time is updated and the specified users are removed.
     */   
@Test
void testUpdateWaitlist_Success() {
    // Arrange
    String waitlistId = "6747ed431b2b466aab4e6479";
    String averageWaitTime = "30";
    List<String> removeUserIds = List.of("austin", "jasmin");

    // Mock existing Waitlist
    Waitlist waitlist = new Waitlist();
    waitlist.setWaitlistId(waitlistId);
    waitlist.setAverageWaitTime("20");
    waitlist.setWaitlisters(new ArrayList<>(List.of("austin", "jasmin"))); 

    // Mock repository behavior
    when(waitlistRepo.findById(waitlistId)).thenReturn(Optional.of(waitlist)); 
    doNothing().when(userWaitListRepository).deleteByUserId(anyString()); 
    when(waitlistRepo.save(any(Waitlist.class))).thenReturn(waitlist); 

    // Act
    boolean result = waitlistServiceImpl.updateWaitlist(waitlistId, averageWaitTime, removeUserIds);

    // Assert
    assertTrue(result); 
    assertEquals("30", waitlist.getAverageWaitTime()); 
    assertFalse(waitlist.getWaitlisters().contains("austin")); 
    assertFalse(waitlist.getWaitlisters().contains("jasmin"));
    verify(waitlistRepo, times(1)).save(waitlist); 
    verify(userWaitListRepository, times(removeUserIds.size())).deleteByUserId(anyString()); 
}

 /**
     * Test case for updating the status of the waitlist (open/close).
     * This test verifies that the waitlist's status is updated successfully.
     */
@Test
void testUpdateWaitlistStatus_Success() {
    // Arrange
    String waitlistId = "6747ed431b2b466aab4e6479";
    String isActive = "false";

    // Mock existing Waitlist
    Waitlist waitlist = new Waitlist();
    waitlist.setWaitlistId(waitlistId);
    waitlist.setIsActive("true"); 

    // Mock repository behavior
    when(waitlistRepo.findById(waitlistId)).thenReturn(Optional.of(waitlist)); 
    when(waitlistRepo.save(any(Waitlist.class))).thenReturn(waitlist); 

    // Act
    boolean result = waitlistServiceImpl.updateWaitlistStatus(waitlistId, isActive);

    // Assert
    assertTrue(result); 
    assertEquals("false", waitlist.getIsActive()); 
    verify(waitlistRepo, times(1)).save(waitlist); 

}

}

