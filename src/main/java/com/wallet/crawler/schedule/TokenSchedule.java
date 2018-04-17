package com.wallet.crawler.schedule;

import com.wallet.crawler.entity.Token;
import com.wallet.crawler.repository.TokenRepository;
import com.wallet.crawler.utils.CrawLerUtil;
import com.wallet.crawler.utils.LogUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:jiangqin@vpgame.cn">Jiangqin</a>
 * @description
 * @date 2018/4/17
 */
@Component
public class TokenSchedule {

    private static final LogUtil logger = LogUtil.newInstance(TokenSchedule.class);

    private Connection connection;
    @Autowired
    private TokenRepository tokenRepository;

//    @Scheduled(cron = "0/10 * * * * *")
    public void fetchToekns() {
        Connection connection = getConnection(1);
        try {
            Document document = connection.get();
            Elements elements = document.getElementById("ContentPlaceHolder1_divpagingpanel").getAllElements();
            String allpage = elements.get(9).text();
            List<Token> tokens = new ArrayList<>();
            for (int i = 1; i <= Integer.valueOf(allpage); i++) {
                Connection temp = getConnection(i);
                Document dt = temp.get();
                Elements trs = dt.getElementsByTag("tbody").get(0).getElementsByTag("tr");
                for (int j = 0; j < trs.size(); j++) {
                    Elements inner = trs.get(j).children();
                    String contractAddress = inner.get(1).child(0).attr("href").substring(7);
                    String alltext = inner.get(2).text();
                    String symbol = alltext.substring(alltext.indexOf("(") + 1, alltext.indexOf(")"));
                    String name = alltext.substring(0, alltext.indexOf("(") - 1);
                    Token token = new Token();
                    token.setContractAddress(contractAddress);
                    token.setName(name);
                    token.setSymbol(symbol);
                    token.setCreatedAt(new Timestamp(new Date().getTime()));
                    token.setUpdatedAt(new Timestamp(new Date().getTime()));
                    tokens.add(token);
                }
                logger.info("访问第"+i+"完成");
            }
            tokens.stream().forEach(tk -> {
                Token token = tokenRepository.findBySymbol(tk.getSymbol());
                if (token == null) {
                    tokenRepository.save(tk);
                }
            });
            logger.info("获取所有token并更新完成！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static Connection getConnection(int page) {
        Connection connection = Jsoup.connect("https://etherscan.io/tokens?p=" + page);
        connection.header("authority", "etherscan.io");
        connection.header("method", "GET");
        connection.header("path", "/tokens?p=" + page);
        connection.header("scheme", "https");
        connection.header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        connection.header("cookie", "__cfduid=d91b76602240aa86cb8847f387b7a452e1521132371; _ga=GA1.2.1779016044.1521132377; ASP.NET_SessionId=h0n115ookwrezfb0ujtwwtff; __cflb=767839123; _gid=GA1.2.444928896.1522677744");
        connection.header("upgrade-insecure-requests", "1");
        connection.header("User-Agent", CrawLerUtil.getRandomUA());
        connection.timeout(50000);
        return connection;
    }
}
