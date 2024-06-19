package com.gr8idea.facebook_clone.post;

import com.gr8idea.facebook_clone.user.internal.AppUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "like_id") private String id;

    @ManyToOne @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

}