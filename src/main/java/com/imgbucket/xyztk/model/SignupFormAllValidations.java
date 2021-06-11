package com.imgbucket.xyztk.model;

import javax.validation.GroupSequence;

@GroupSequence({ SignupFormValidations.Group1.class, SignupFormValidations.Group2.class, SignupFormValidations.Group3.class, SignupFormValidations.Group4.class, SignupFormValidations.Group5.class, SignupFormValidations.Group6.class, SignupFormValidations.Group7.class,
        SignupFormValidations.Group8.class, SignupFormValidations.Group9.class })
public interface SignupFormAllValidations {

}