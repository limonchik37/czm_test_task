package fit.cvut.cz.console.pages;

import fit.cvut.cz.console.TextPage;

public class StartPage implements TextPage {
    @Override
    public String load() {
        return "Hi! You started cyclist stats application upload your file and command or type --guide for familiarization with program.\n";
    }
}
