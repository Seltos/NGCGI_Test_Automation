package web_selenium.util;

import net.sourceforge.tess4j.ITessAPI.TessPageIteratorLevel;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class TesseractOcr {

    ITesseract tess;

    public TesseractOcr() {
        this.tess = new Tesseract();
        this.tess.setDatapath(System.getProperty("user.dir"));
//        this.tess.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_CUBE_ONLY);
    }

    public List<Word> getLines(BufferedImage image) {
        try {
            List<Word> words = tess.getWords(image, TessPageIteratorLevel.RIL_TEXTLINE);
            return words;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to do OCR", ex);
        }
    }

    public String getText(BufferedImage image) {
        try {
            return this.tess.doOCR(image);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to do OCR", ex);
        }
    }

    public List<Word> getWords(BufferedImage image) {
        try {
            List<Word> words = this.tess.getWords(image, TessPageIteratorLevel.RIL_WORD);
            return words;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to do OCR", ex);
        }
    }

    public void setPageSegMode(int pageSegMode) {
        this.tess.setPageSegMode(pageSegMode);
    }
    
    public void setTessVariable(String varName, String varValue) {
        if (varName != null && varValue != null) {
            this.tess.setTessVariable(varName, varValue);
        }
    }

    public void setTessVariables(Map<String, String> tessVariables) {
        if (tessVariables != null) {
            for (Map.Entry<String, String> variable : tessVariables.entrySet()) {
                this.setTessVariable(variable.getKey(), variable.getValue());
            }
        }
    }
}
