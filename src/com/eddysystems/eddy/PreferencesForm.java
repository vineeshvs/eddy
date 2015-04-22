package com.eddysystems.eddy;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreferencesForm {

  private JCheckBox applyAutomaticallyOnEnterCheckBox;
  private JPanel panel;
  private JTextField autoApplyThresholdTextField;
  private JTextField autoApplyFactorTextField;
  private JTextField minProbabilityTextField;
  private JTextField minRelativeProbability;
  private JButton resetButton;
  private JCheckBox removeQualifiersCheckBox;
  private JTextField startDelay;
  private JTextField contactEmailTextField;
  private JRadioButton logNormallyRadioButton;
  private JRadioButton noCodeLoggingRadioButton;
  private JRadioButton noLoggingRadioButton;
  private JFormattedTextField licenseCodeTextField;

  public PreferencesForm() {
    resetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Preferences.resetToDefaults();
      }
    });
    licenseCodeTextField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        check();
      }
      @Override
      public void removeUpdate(DocumentEvent documentEvent) {
        check();
      }
      @Override
      public void changedUpdate(DocumentEvent documentEvent) {
        check();
      }

      // enable/disable code logging preferences based on validity of license code
      private void check() {
        boolean e = Preferences.checkLicense(licenseCodeTextField.getText());
        if (!e)
          logNormallyRadioButton.setSelected(true);
        logNormallyRadioButton.setEnabled(e);
        noCodeLoggingRadioButton.setEnabled(e);
        noLoggingRadioButton.setEnabled(e);
      }
    });
  }

  public PreferenceData.LogPreference getLogPreference() {
    if (logNormallyRadioButton.isSelected())
      return PreferenceData.LogPreference.Normal;
    else if (noCodeLoggingRadioButton.isSelected())
      return PreferenceData.LogPreference.NoCode;
    else if (noLoggingRadioButton.isSelected())
      return PreferenceData.LogPreference.NoLog;
    else
      throw new IllegalStateException();
  }

  public void setLogPreference(PreferenceData.LogPreference lp) {
    if (lp == PreferenceData.LogPreference.Normal)
      logNormallyRadioButton.setSelected(true);
    else if (lp == PreferenceData.LogPreference.NoCode)
      noCodeLoggingRadioButton.setSelected(true);
    else if (lp == PreferenceData.LogPreference.NoLog)
      noLoggingRadioButton.setSelected(true);
  }

  public JPanel getPanel() {
    return panel;
  }

  public void setData(PreferenceData data) {
    applyAutomaticallyOnEnterCheckBox.setSelected(data.isAutoApply());
    autoApplyThresholdTextField.setText(data.getAutoApplyThreshold());
    autoApplyFactorTextField.setText(data.getAutoApplyFactor());
    minProbabilityTextField.setText(data.getMinProbability());
    minRelativeProbability.setText(data.getMinRelativeProbability());
    removeQualifiersCheckBox.setSelected(data.isRemoveQualifiers());
    startDelay.setText(data.getStartDelay());
    contactEmailTextField.setText(data.getEmail());
    licenseCodeTextField.setText(data.getLicenseCode());
    setLogPreference(data.getLogPreference());
  }

  public void getData(PreferenceData data) {
    data.setAutoApply(applyAutomaticallyOnEnterCheckBox.isSelected());
    data.setAutoApplyThreshold(autoApplyThresholdTextField.getText());
    data.setAutoApplyFactor(autoApplyFactorTextField.getText());
    data.setMinProbability(minProbabilityTextField.getText());
    data.setMinRelativeProbability(minRelativeProbability.getText());
    data.setRemoveQualifiers(removeQualifiersCheckBox.isSelected());
    data.setStartDelay(startDelay.getText());
    data.setEmail(contactEmailTextField.getText());
    data.setLicenseCode(licenseCodeTextField.getText());
    data.setLogPreference(getLogPreference());
  }

  public boolean isModified(PreferenceData data) {
    if (applyAutomaticallyOnEnterCheckBox.isSelected() != data.isAutoApply()) return true;
    if (autoApplyThresholdTextField.getText() != null ? !autoApplyThresholdTextField.getText().equals(data.getAutoApplyThreshold()) : data.getAutoApplyThreshold() != null)
      return true;
    if (autoApplyFactorTextField.getText() != null ? !autoApplyFactorTextField.getText().equals(data.getAutoApplyFactor()) : data.getAutoApplyFactor() != null)
      return true;
    if (minProbabilityTextField.getText() != null ? !minProbabilityTextField.getText().equals(data.getMinProbability()) : data.getMinProbability() != null)
      return true;
    if (minRelativeProbability.getText() != null ? !minRelativeProbability.getText().equals(data.getMinRelativeProbability()) : data.getMinRelativeProbability() != null)
      return true;
    if (removeQualifiersCheckBox.isSelected() != data.isRemoveQualifiers()) return true;
    if (startDelay.getText() != null ? !startDelay.getText().equals(data.getStartDelay()) : data.getStartDelay() != null)
      return true;
    if (contactEmailTextField.getText() != null ? !contactEmailTextField.getText().equals(data.getEmail()) : data.getEmail() != null)
      return true;
    if (licenseCodeTextField.getText() != null ? !licenseCodeTextField.getText().equals(data.getLicenseCode()) : data.getLicenseCode() != null)
      return true;
    if (data.getLogPreference() != getLogPreference())
      return true;
    return false;
  }
}
