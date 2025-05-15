package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.internal.web.UserTestData;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL_PROFILE = ProfileRestController.REST_URL;

    ProfileTo incorrectProfileTO;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_PROFILE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(USER_PROFILE_TO));

    }
    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_PROFILE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateProfile() throws Exception {
        ProfileTo testProfileTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(testProfileTo)))
                .andExpect(status().isNoContent());
        perform(MockMvcRequestBuilders.get(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(PROFILE_TO_MATCHER.contentJson(getUpdatedTo()));
    }
     @Test
     @WithUserDetails(value = UserTestData.USER_MAIL)
     void updateProfileToWithUnknownContact() throws Exception {
        incorrectProfileTO = getWithUnknownContactTo();
        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(incorrectProfileTO)))
                .andExpect(status().isUnprocessableEntity());
     }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateProfileToWithContactHtmlUnsafe() throws Exception {
        incorrectProfileTO = getWithContactHtmlUnsafeTo();
        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(incorrectProfileTO)))
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateProfileToWithUnknownNotification() throws Exception {
        incorrectProfileTO = getWithUnknownNotificationTo();
        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(incorrectProfileTO)))
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateProfileInvalidTo() throws Exception {
        incorrectProfileTO = getInvalidTo();
        perform(MockMvcRequestBuilders.put(REST_URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(incorrectProfileTO)))
                .andExpect(status().isUnprocessableEntity());
    }
}