package com.wallet.crawler.repository;

import com.wallet.crawler.entity.TokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author <a href="mailto:jiangqin@vpgame.cn">Jiangqin</a>
 * @description
 * @date 2018/4/17
 */
public interface TokenHistoryRepository extends CrudRepository<TokenHistory, Long>, JpaRepository<TokenHistory, Long>, JpaSpecificationExecutor<TokenHistory> {

}