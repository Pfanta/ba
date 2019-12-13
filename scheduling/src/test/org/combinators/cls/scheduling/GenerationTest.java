package org.combinators.cls.scheduling;

import org.combinators.cls.scheduling.model.ShopClass;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.GenerationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenerationTest {
    @Test
    public void testGenerationFlowShopWithoutDeadlines() {
        Task task = GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 5, false, ShopClass.FS));
        ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
        assertEquals(5, classification.getJobCount());
        assertEquals(4, classification.getMachineCount());
        assertEquals(ShopClass.FS, classification.getShopClass());
        assertFalse(classification.isDeadlines());
    }

    @Test
    public void testGenerationFlowShopWithDeadlines() {
        Task task = GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 5, true, ShopClass.FS));
        ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
        assertEquals(5, classification.getJobCount());
        assertEquals(4, classification.getMachineCount());
        assertEquals(ShopClass.FS, classification.getShopClass());
        assertTrue(classification.isDeadlines());
    }

    @Test
    public void testGenerationFlexibleFlowShopWithoutDeadlines() {
        Task task = GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 5, false, ShopClass.FFS));
        ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
        assertEquals(5, classification.getJobCount());
        assertEquals(4, classification.getMachineCount());
        assertEquals(ShopClass.FFS, classification.getShopClass());
        assertFalse(classification.isDeadlines());
    }

    @Test
    public void testGenerationFlexibleFlowShopWithDeadlines() {
        Task task = GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 5, true, ShopClass.FFS));
        ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
        assertEquals(5, classification.getJobCount());
        assertEquals(4, classification.getMachineCount());
        assertEquals(ShopClass.FFS, classification.getShopClass());
        assertTrue(classification.isDeadlines());
    }

    @Test
    public void testGenerationJobShopWithoutDeadlines() {
        Task task = GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 5, false, ShopClass.JS));
        ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
        assertEquals(5, classification.getJobCount());
        assertEquals(4, classification.getMachineCount());
        assertEquals(ShopClass.JS, classification.getShopClass());
        assertFalse(classification.isDeadlines());
    }

    @Test
    public void testGenerationJobShopWithDeadlines() {
        Task task = GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 5, true, ShopClass.JS));
        ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
        assertEquals(5, classification.getJobCount());
        assertEquals(4, classification.getMachineCount());
        assertEquals(ShopClass.JS, classification.getShopClass());
        assertTrue(classification.isDeadlines());
    }

    @Test
    public void testGenerationFlexibleJobShopWithoutDeadlines() {
        Task task = GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 5, false, ShopClass.FJS));
        ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
        assertEquals(5, classification.getJobCount());
        assertEquals(4, classification.getMachineCount());
        assertEquals(ShopClass.FJS, classification.getShopClass());
        assertFalse(classification.isDeadlines());
    }

    @Test
    public void testGenerationFlexibleJobShopWithDeadlines() {
        Task task = GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 5, true, ShopClass.FJS));
        ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
        assertEquals(5, classification.getJobCount());
        assertEquals(4, classification.getMachineCount());
        assertEquals(ShopClass.FJS, classification.getShopClass());
        assertTrue(classification.isDeadlines());
    }
    
    @Test
    public void testGenerationZeroJobs() {
        assertThrows(IllegalArgumentException.class, () -> GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 0, true, ShopClass.FS)));
    }
    
    @Test
    public void testGenerationZeroMachines() {
        assertThrows(IllegalArgumentException.class, () -> GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(0, 5, true, ShopClass.FS)));
    }
    
    @Test
    public void testGenerationNegativeJobs() {
        assertThrows(IllegalArgumentException.class, () -> GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, -1, true, ShopClass.FS)));
    }
    
    @Test
    public void testGenerationNegativeMachines() {
        assertThrows(IllegalArgumentException.class, () -> GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(-1, 5, true, ShopClass.FS)));
    }
    
    @Test
    public void testGenerationNoShopClass() {
        assertThrows(IllegalArgumentException.class, () -> GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(4, 5, true, ShopClass.NONE)));
    }
}
