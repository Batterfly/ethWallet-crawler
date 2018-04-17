package com.wallet.crawler.schedule;

import com.wallet.crawler.async.AsyncSaveDbService;
import com.wallet.crawler.entity.Token;
import com.wallet.crawler.entity.TokenWallet;
import com.wallet.crawler.repository.TokenRepository;
import com.wallet.crawler.repository.TokenWalletRepository;
import com.wallet.crawler.utils.CrawLerUtil;
import com.wallet.crawler.utils.LogUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:jiangqin@vpgame.cn">Jiangqin</a>
 * @description
 * @date 2018/4/18
 */
@Component
public class TokenHolderSchedule {

    private static final LogUtil logger = LogUtil.newInstance(TokenHolderSchedule.class);
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private TokenWalletRepository tokenWalletRepository;

//    @Scheduled(cron = "0/10 * * * * *")
    public void fetchTokenHolder() {
        List<Token> tokens = tokenRepository.findAll();
        if (CollectionUtils.isEmpty(tokens)) {
            logger.info("tokens为空");
            return;
        }
        tokens.stream().forEach(token -> {
            long id = token.getId();
            String contractAddress = token.getContractAddress();
            List<TokenWallet> tokenWallets = new ArrayList<>();
            Connection connection = getConnection(contractAddress);
            try {
                Document document = connection.get();
                Elements elements = document.getElementsByTag("tbody").get(0).getElementsByTag("tr");
                elements.remove(0);

                for (int i = 0; i < 30; i++) {
                    TokenWallet wallet = new TokenWallet();
                    Elements temp = elements.get(i).children();
                    String walletAddress = temp.get(1).child(0).text();
                    wallet.setAddress(walletAddress);
                    wallet.setTokenId(id);
                    wallet.setCreatedAt(new Timestamp(new Date().getTime()));
                    wallet.setUpdatedAt(new Timestamp(new Date().getTime()));
                    tokenWallets.add(wallet);
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            tokenWallets.stream().forEach(wallet -> {
                TokenWallet tw = tokenWalletRepository.findByTokenIdAndAddress(wallet.getTokenId(),wallet.getAddress());
                if (tw == null) {
                    tokenWalletRepository.save(tw);
                }
            });
            logger.info("刷新：" + token.getSymbol() + "前三十名钱包成功");
        });
        logger.info("刷新所有钱包成功");
    }

    public static Connection getConnection(String contractAddress) {
        Connection connection = Jsoup.connect("https://etherscan.io/token/generic-tokenholders2?a=" + contractAddress + "&s=100000000000000000");
        connection.header("authority", "etherscan.io");
        connection.header("method", "GET");
        connection.header("path", "/token/generic-tokenholders2?a=" + contractAddress + "&s=100000000000000000");
        connection.header("scheme", "https");
        connection.header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        connection.header("cookie", "__cfduid=d91b76602240aa86cb8847f387b7a452e1521132371; _ga=GA1.2.1779016044.1521132377; ASP.NET_SessionId=h0n115ookwrezfb0ujtwwtff; __cflb=767839123; _gid=GA1.2.444928896.1522677744");
        connection.header("upgrade-insecure-requests", "1");
        connection.header("User-Agent", CrawLerUtil.getRandomUA());
        connection.timeout(50000);
        return connection;
    }
}
