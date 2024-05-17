package com.inturn.pfit.support.fixture;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommonResponseResultFixture {

	public static ResultActions successResultActions(ResultActions actions) throws Exception {
		return actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andDo(print());
	}

	public static ResultActions failResultActions(ResultActions actions, ResultMatcher status) throws Exception {
		return actions
				.andExpect(status)
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

}
