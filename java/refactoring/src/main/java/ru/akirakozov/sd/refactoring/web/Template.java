package ru.akirakozov.sd.refactoring.web;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Template {

    private static final MustacheFactory factory = new DefaultMustacheFactory();
    private static final Map<String, Mustache> cache = new ConcurrentHashMap<>();

    private final Mustache mustache;

    private final Map<String, Object> context = new HashMap<>();

    private Template(Mustache mustache) {
        this.mustache = mustache;
    }

    public static Template load(String resource) {
        if (!cache.containsKey(resource)) {
            cache.put(resource, factory.compile(resource));
        }
        return new Template(cache.get(resource));
    }

    public Template set(String key, Object value) {
        context.put(key, value);
        return this;
    }

    public void render(Writer writer) {
        mustache.execute(writer, context);
    }
}
