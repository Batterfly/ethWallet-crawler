package com.wallet.crawler.repository;

import com.wallet.crawler.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author <a href="mailto:jiangqin@vpgame.cn">Jiangqin</a>
 * @description
 * @date 2018/4/17
 */
public interface TokenRepository extends CrudRepository<Token, Long>, JpaRepository<Token, Long>, JpaSpecificationExecutor<Token> {
    Token findBySymbol(String symobol);
}