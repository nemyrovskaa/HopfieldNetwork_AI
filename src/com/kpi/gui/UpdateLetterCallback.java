package com.kpi.gui;

import com.kpi.Letter;

public interface UpdateLetterCallback {
    void updatePredictPanelCallback();
    void updateAddPanelCallback(Letter updatedLetter);
}
