package com.wallet.crawler.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author <a href="mailto:jiangqin@vpgame.cn">Jiangqin</a>
 * @description
 * @date 2018/4/17
 */
@Entity
@Table(name = "token_history", schema = "tokenmonitor", catalog = "")
public class TokenHistory {
    private long id;
    private long tokenId;
    private long tokenWalletId;
    private String txHash;
    private int type;
    private BigDecimal count;
    private Timestamp createAt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "token_id", nullable = false)
    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    @Basic
    @Column(name = "token_wallet_id", nullable = false)
    public long getTokenWalletId() {
        return tokenWalletId;
    }

    public void setTokenWalletId(long tokenWalletId) {
        this.tokenWalletId = tokenWalletId;
    }

    @Basic
    @Column(name = "tx_hash", nullable = false, length = 512)
    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    @Basic
    @Column(name = "type", nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "count", nullable = false, precision = 8)
    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    @Basic
    @Column(name = "create_at", nullable = false)
    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

}
