package com.batch;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@StepScope
@Component
public class ComicCharacterReader implements ItemReader<ComicCharacter> {

    private boolean isVillain;
    private Iterator<ComicCharacter> iterator = Collections.emptyIterator();

    @BeforeStep
    public void captureJobParameter(StepExecution stepExecution) {
        String isVillainString = stepExecution.getJobParameters().getString("isVillain", "false");
        isVillain = Boolean.parseBoolean(isVillainString);
        initializeCharacterList(isVillain);
    }

    private void initializeCharacterList(boolean isVillain) {
        if(isVillain) {
            iterator = DataUtil.villainCharacters.iterator();
        } else {
            iterator = DataUtil.heroCharacters.iterator();
        }
    }

    @Override
    public ComicCharacter read() {
        if(iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
}
