package View;

public class ExitCommand extends Command {
    public ExitCommand(String option, String description){
        super(option,description);
    }

    @Override
    public void execute() {
        System.exit(0);
    }

}
