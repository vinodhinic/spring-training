package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ComicCharacterProcessor implements ItemProcessor<ComicCharacter,String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComicCharacterProcessor.class);
    private String universe;

    public ComicCharacterProcessor(String universe) {
        this.universe = universe;
    }
    @Override
    public String process(ComicCharacter item) throws Exception {
        LOGGER.info("Processing item {} ", item);
        if (item.getUniverse().equals(universe)) {
            return item.getName().toUpperCase();
        }
        return null;
    }
}
