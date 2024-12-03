package com.helmo.greenThumb.dto;

import com.helmo.greenThumb.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

public class CommentDTO {
    private User user;

    private Date date;

    private String text;
}
