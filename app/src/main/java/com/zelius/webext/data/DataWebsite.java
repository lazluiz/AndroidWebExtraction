package com.zelius.webext.data;

import com.zelius.webext.model.ContentModel;
import com.zelius.webext.model.SectionModel;

import java.util.ArrayList;

/**
 * Created by Zelius on 29/03/2015.
 */
public class DataWebsite {

    public static ArrayList<ContentModel> dataContentSelector = new ArrayList<ContentModel>(){{
        add(new ContentModel(EnumContents.HEADER_CATEGORY, "header.article-header h2.section-name > a"));
        add(new ContentModel(EnumContents.HEADER_DATE, "header.article-header abbr.published > span.published-date"));
        add(new ContentModel(EnumContents.TITLE, "header.article-header h1.article-title"));
        //add(new ContentModel(EnumContents.SUBTITLE, "header.article-header > h2.materia-subtitulo"));
        add(new ContentModel(EnumContents.PICTURE, "div.article-body div.article-photo > div > img"));
        add(new ContentModel(EnumContents.BODY, "div.article-body div.entry-content"));
    }};

    public static ArrayList<SectionModel> dataSectionLinks = new ArrayList<SectionModel>(){{
        add(new SectionModel(EnumSections.ULTIMAS_NOTICIAS, "Últimas Notícias", "http://dc.clicrbs.com.br/sc/noticias/ultimas-noticias-rss/"));
        add(new SectionModel(EnumSections.ECONOMIA, "Economia", "http://dc.clicrbs.com.br/sc/noticias/ultimas-noticias-rss/tag/economia"));
        add(new SectionModel(EnumSections.POLITICA, "Política", "http://dc.clicrbs.com.br/sc/noticias/ultimas-noticias-rss/tag/politica/"));
        add(new SectionModel(EnumSections.MUNDO, "Mundo", "http://dc.clicrbs.com.br/sc/noticias/ultimas-noticias-rss/tag/mundo/"));
        add(new SectionModel(EnumSections.ESPORTES, "Esportes", "http://dc.clicrbs.com.br/sc/esportes/ultimas-noticias-rss/"));
        add(new SectionModel(EnumSections.VARIEDADES, "Cacau Menezes", "http://dc.clicrbs.com.br/sc/colunistas/cacau-menezes/ultimas-noticias-rss/"));
        add(new SectionModel(EnumSections.POLICIA, "Polícia", "http://dc.clicrbs.com.br/sc/noticias/ultimas-noticias/tag/policia/"));
    }};

    public enum EnumSections {
        ULTIMAS_NOTICIAS,
        ECONOMIA,
        POLICIA,
        POLITICA,
        MUNDO,
        ESPORTES,
        VARIEDADES
    }

    public enum EnumContents {
        HEADER_CATEGORY,
        HEADER_DATE,
        TITLE,
        SUBTITLE,
        PICTURE,
        BODY,
        FOOTER
    }
}


