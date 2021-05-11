package com.imagepot.xyztk.model;

import javax.validation.GroupSequence;
import com.imagepot.xyztk.model.UserInfoValidations.*;

@GroupSequence({ Group1.class, Group2.class, Group3.class})
public interface UserInfoAllValidations {
}
