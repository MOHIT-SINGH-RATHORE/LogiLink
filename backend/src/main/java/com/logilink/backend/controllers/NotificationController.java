package com.logilink.backend.controllers;

import com.logilink.backend.entity.Notification;
import com.logilink.backend.repository.NotificationRepository;
import com.logilink.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    @PreAuthorize("hasRole('TRANSPORTER')")
    public ResponseEntity<?> getMyNotifications(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Notification> notifications = notificationRepository
                .findByRecipientIdOrderByCreatedAtDesc(userDetails.getId());
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    @PreAuthorize("hasRole('TRANSPORTER')")
    public ResponseEntity<?> getUnreadNotifications(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Notification> notifications = notificationRepository
                .findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(userDetails.getId());
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('TRANSPORTER')")
    public ResponseEntity<?> markAsRead(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Notification> notifOpt = notificationRepository.findById(id);

        if (notifOpt.isPresent()) {
            Notification notification = notifOpt.get();
            if (notification.getRecipientId().equals(userDetails.getId())) {
                notification.setRead(true);
                notificationRepository.save(notification);
                return ResponseEntity.ok("Notification marked as read");
            } else {
                return ResponseEntity.status(403).body("Error: Unauthorized to modify this notification");
            }
        }
        return ResponseEntity.notFound().build();
    }
}
