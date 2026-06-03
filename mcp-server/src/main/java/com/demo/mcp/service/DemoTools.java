package com.demo.mcp.service;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DemoTools {

//    @Tool(name = "calculator", description = "Evaluate a mathematical expression and return the result")
//    public String calculator(@ToolParam(description = "Mathematical expression, e.g. '2 + 3 * 5', '(10 + 5) / 2'") String expression) {
//        try {
//            double result = evaluateExpression(expression);
//            return "Result of " + expression + " = " + result;
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
//
//    @Tool(name = "getCurrentTime", description = "Get current server time")
//    public String getCurrentTime(@ToolParam(description = "Timezone, e.g. 'UTC', 'Asia/Shanghai', 'America/New_York'") String timezone) {
//        String tz = (timezone != null && !timezone.isBlank()) ? timezone : "UTC";
//        return "Current server time (UTC): " + Instant.now() + "\nRequested timezone: " + tz;
//    }
//
//    @Tool(name = "greet", description = "Send a personalized greeting")
//    public String greet(
//            @ToolParam(description = "Name of the person to greet") String name,
//            @ToolParam(description = "Language: 'en', 'zh', 'ja', 'fr', 'de'") String language) {
//        String lang = (language != null && !language.isBlank()) ? language : "en";
//        return switch (lang) {
//            case "zh" -> "你好, " + name + "! 很高兴见到你!";
//            case "ja" -> "こんにちは, " + name + "! お会いできて光栄です!";
//            case "fr" -> "Bonjour, " + name + "! Ravi de vous rencontrer!";
//            case "de" -> "Hallo, " + name + "! Freut mich, Sie kennenzulernen!";
//            default -> "Hello, " + name + "! Great to meet you!";
//        };
//    }
//
//    @Tool(name = "getServerInfo", description = "Get information about this MCP server")
//    public String getServerInfo() {
//        return """
//            Demo MCP Server v1.0.0
//            ========================
//            Server Name: demo-mcp-server
//            Description: A demo MCP server with useful tools for mcp-context-forge
//            Transport: Streamable HTTP
//            Available Tools:
//              - calculator: Evaluate mathematical expressions
//              - getCurrentTime: Get current server time
//              - greet: Send a greeting to someone
//              - getWeather: Get weather for a city
//              - getServerInfo: Get server information
//            """;
//    }

    @Tool(name = "getWeather", description = "Get weather information for a city")
    public String getWeather(@ToolParam(description = "City name") String city) {
        var weatherData = java.util.Map.ofEntries(
                java.util.Map.entry("beijing", "☀️ Sunny, 28°C, Humidity: 45%"),
                java.util.Map.entry("shanghai", "🌤️ Partly cloudy, 25°C, Humidity: 60%"),
                java.util.Map.entry("new york", "🌧️ Rainy, 18°C, Humidity: 75%"),
                java.util.Map.entry("london", "☁️ Cloudy, 15°C, Humidity: 70%"),
                java.util.Map.entry("tokyo", "🌸 Clear, 22°C, Humidity: 55%")
        );
        String normalized = city.toLowerCase().trim();
        if (weatherData.containsKey(normalized)) {
            System.out.println("Weather in " + city + ": " + weatherData.get(normalized));
            return "Weather in " + city + ": " + weatherData.get(normalized);
        }
        System.out.println("Weather in " + city + ": ☀️ Sunny, 23°C, Humidity: 50% (demo data)");
        return "Weather in " + city + ": ☀️ Sunny, 23°C, Humidity: 50% (demo data)";
    }

    private double evaluateExpression(String expr) {
        expr = expr.replaceAll("\\s+", "");
        return eval(expr, 0);
    }

    private double eval(String expr, int pos) {
        double x = parseTerm(expr, pos);
        while (pos < expr.length()) {
            char op = expr.charAt(pos);
            if (op != '+' && op != '-') break;
            pos++;
            double y = parseTerm(expr, pos);
            if (op == '+') x += y;
            else x -= y;
        }
        return x;
    }

    private double parseTerm(String expr, int pos) {
        double x = parsePower(expr, pos);
        while (pos < expr.length()) {
            char op = expr.charAt(pos);
            if (op != '*' && op != '/' && op != '%') break;
            pos++;
            double y = parsePower(expr, pos);
            if (op == '*') x *= y;
            else if (op == '/') x /= y;
            else x %= y;
        }
        return x;
    }

    private double parsePower(String expr, int pos) {
        double x = parseFactor(expr, pos);
        int powPos = expr.indexOf('^', pos);
        if (powPos == -1) return x;
        pos = powPos + 1;
        double y = parseFactor(expr, pos);
        return Math.pow(x, y);
    }

    private double parseFactor(String expr, int pos) {
        if (pos >= expr.length()) return 0;
        char c = expr.charAt(pos);
        if (c == '(') {
            int depth = 1;
            int start = pos + 1;
            while (++pos < expr.length() && depth > 0) {
                if (expr.charAt(pos) == '(') depth++;
                else if (expr.charAt(pos) == ')') depth--;
            }
            return eval(expr.substring(start, pos), 0);
        }
        StringBuilder sb = new StringBuilder();
        if (c == '-') { sb.append(c); pos++; }
        while (pos < expr.length() && (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos) == '.')) {
            sb.append(expr.charAt(pos++));
        }
        if (sb.length() == 0) return 0;
        return Double.parseDouble(sb.toString());
    }
}