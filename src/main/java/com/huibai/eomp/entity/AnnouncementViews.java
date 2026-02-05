package com.huibai.eomp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 公告查看记录表
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Getter
@Setter
@TableName("announcement_views")
public class AnnouncementViews implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("announcement_id")
    private Integer announcementId;

    @TableField("user_id")
    private Integer userId;

    @TableField("viewed_at")
    private LocalDateTime viewedAt;
}
