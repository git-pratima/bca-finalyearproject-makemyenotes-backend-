package com.pratima.makemyenotes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Immutable                                          // org.hibernate.annotations.Immutable
@Subselect("SELECT * FROM vw_transaction")        // org.hibernate.annotations.Subselect
@Synchronize("vw_transaction")                        // org.hibernate.annotations.Synchronize
@Table(name = "vw_transaction")
public class VwTransaction {

    @Id
    private Long pid;

    private Long id;

    private String topic;

    private String category;

    private String subCategory;

    private String type;

    @NotBlank(message = "Name is required")
    private String question;

    @Column(length = 4000)
    private String answer;

    @Column(name = "bookmarked")
    private String bookmark;

    private String level;

    private Long userId;

    @Column(name = "createdbycurrentuser")
    private String createdByCurrentUser;

    @Lob
    private byte[] image;

    @Column(name="imageurl")
    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String createdBy;

    private Date modifiedDate;

    private String modifiedBy;

}
