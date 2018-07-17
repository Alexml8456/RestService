package com.alex;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Main {
    public static void main(String[] args) {


        StringBuilder builder = new StringBuilder();
        builder.append("------Wave Trend indicators------\n");
        builder.append("---------------------------------");



        System.out.println(builder.toString());
        System.out.println(getGMTTimeMillis());


        VelocityEngine ve = new VelocityEngine();
        ve.init();
        Template t = ve.getTemplate( "src/main/resources/helloworld.vm" );
        VelocityContext context = new VelocityContext();
        context.put("name", "World");
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        System.out.println(writer.toString());
    }

    public static LocalDateTime getGMTTimeMillis() {
        return LocalDateTime.now(ZoneId.of("GMT-0"));
    }
}