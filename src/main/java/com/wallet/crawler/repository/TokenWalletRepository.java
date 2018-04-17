package com.wallet.crawler.repository;

import com.wallet.crawler.entity.TokenWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author <a href="mailto:jiangqin@vpgame.cn">Jiangqin</a>
 * @description
 * @date 2018/4/17
 */
public interface TokenWalletRepository extends CrudRepository<TokenWallet, Long>, JpaRepository<TokenWallet, Long>, JpaSpecificationExecutor<TokenWallet> {
    TokenWallet findByTokenIdAndAddress(long tokenId,String address);
}