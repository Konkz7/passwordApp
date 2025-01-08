package com.example.password.Models;

import androidx.lifecycle.ViewModel;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.RepeatCharacterRegexRule;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import java.util.Arrays;
import java.util.List;


public class GenModel extends ViewModel {
    private static final int MAX_PASSWORD_LENGTH = 15;

    private String generated;

    public String getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        this.generated = generated;
    }

    public  String generatePassword(Integer length, Integer numUppercase, Integer numDigits, Integer numSpecial) {

        if (length < numUppercase + numDigits + numSpecial) {
            length = numDigits + numSpecial + numUppercase + 1;
        } else if (length > MAX_PASSWORD_LENGTH) {
            length = MAX_PASSWORD_LENGTH;
        }

        PasswordGenerator generator = new PasswordGenerator();
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
        upperCaseRule.setNumberOfCharacters(numUppercase);

        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit);
        digitRule.setNumberOfCharacters(numDigits);

        CharacterRule specialCharRule = new CharacterRule(new CharacterData() {
            public String getErrorCode() {
                return "INSUFFICIENT_SPECIAL";
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        });
        specialCharRule.setNumberOfCharacters(numSpecial);

        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);

        // Remaining characters will be lowercase to fill up the required length
        int numLowercase = length - (numUppercase + numDigits + numSpecial);

        if (numLowercase > 0) {
            lowerCaseRule.setNumberOfCharacters(numLowercase);
        } else {
            lowerCaseRule.setNumberOfCharacters(0);
        }

        List<CharacterRule> rules = Arrays.asList(upperCaseRule, digitRule, specialCharRule, lowerCaseRule);
        generated = generator.generatePassword(length, rules);
        return generated;
    }

    public int measurePasswordStrength(String password) {
        // Define password rules
        List<Rule> rules = Arrays.asList(
                new LengthRule(8, 16), // Password must be 8-16 characters
                new CharacterRule(EnglishCharacterData.UpperCase, 1), // At least 1 uppercase letter
                new CharacterRule(EnglishCharacterData.LowerCase, 1), // At least 1 lowercase letter
                new CharacterRule(EnglishCharacterData.Digit, 1), // At least 1 digit
                new CharacterRule(EnglishCharacterData.Special, 1), // At least 1 special character
                new RepeatCharacterRegexRule(3),//No more than 2 repeating characters
                new WhitespaceRule() // No whitespace
        );

        // Validate password against the rules


        // Calculate strength score
        int score = 0;
        for (int i = 0 ; i < rules.size() ; i++) {
            PasswordValidator validator = new PasswordValidator(rules.get(i));
            RuleResult ruleResult = validator.validate(new PasswordData(password));
            if (ruleResult.isValid()) {
                score++;
            }else{
                if(i == 0){
                    int x = password.length() >= 5 ? 2 : 1;
                    return x;
                } else if (i == 5) {
                    return 2;
                }
            }
        }

        // Return strength score (number of rules satisfied)
        return score;
    }
}

