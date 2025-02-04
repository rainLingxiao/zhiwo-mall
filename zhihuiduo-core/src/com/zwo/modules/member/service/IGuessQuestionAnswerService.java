/**
 * 
 */
package com.zwo.modules.member.service;

import com.zwo.modules.member.domain.GuessQuestionAnswer;
import com.zwotech.modules.core.service.IBaseService;

/**
 * @author hjx
 *
 */
public interface IGuessQuestionAnswerService extends IBaseService<GuessQuestionAnswer> {
	/**
	 * 结算会员账号。
	 * @param answer
	 */
	void settleAccounts(GuessQuestionAnswer answer);
}
