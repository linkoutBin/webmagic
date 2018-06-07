package com.bin.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class GithubRepoPageProcess implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(http://localhost:8080/\\w+/\\w+)").all());
        page.putField("author", page.getUrl().regex("http://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//div[@class='uk-width-1-1 uk-margin']/h1/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(true);
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        //System.out.println("协议："+System.getProperty("https.protocols"));
        //System.setProperty("https.protocols", "SSLv2");
        //System.setProperty("javax.net.debug", "all");
        Spider.create(new GithubRepoPageProcess()).addPipeline(new ConsolePipeline()).addUrl("http://localhost:8080/static/index.html").thread(5).run();
    }

}
