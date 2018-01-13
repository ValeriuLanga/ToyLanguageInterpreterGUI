package Controller;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.FileException;
import Model.Exceptions.UnknownOperationException;
import Model.ProgramState;
import Model.Repository.RepositoryInterface;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller{
    private RepositoryInterface repository;
    private ExecutorService executorService;

    public Controller(RepositoryInterface repository){
        this.repository = repository;
    }

    /*
    public void executeOnce() throws DivisionByZeroException, UnknownOperationException, IOException, HeapException {
        ProgramState programState = repository.getCurrentProgramState();

        if(!programState.getExecutionStack().isEmpty())
        {
            Statement statement = programState.getExecutionStack().pop();
            statement.execute(programState);
        }

        System.out.println(programState);
        repository.logProgramState();

        repository.addProgramState(programState);
    }
    */

    public void executeOnceAllPrograms(List<ProgramState> programStates) throws FileException, InterruptedException {
        // log the state
        programStates.forEach(program -> {
            try {
                repository.logProgramState(program);
            } catch (IOException e) {
                throw new FileException(e.getMessage());
            }
        });

        // run concurrently one step for each program
        // prepare the list of callables
        List<Callable<ProgramState>> callablesList = programStates.stream()
                .map((ProgramState programState) -> (Callable<ProgramState>)(()->{return programState.executeOnce();}))
                .collect(Collectors.toList());

        // start the execution
        // will return a list of threads
        List<ProgramState> newProgramsList = executorService.invokeAll(callablesList)
                .stream().map(future -> {
                    try{
                        return future.get();
                    }
                    catch(Exception e){
                        //
                        //  Read and Write Heap cause an exception to be thrown here!!!!
                        //
                        //throw new ExecutionStackException(e.getMessage());
                        System.out.println(e);
                    }
                    return null;
                    }).filter(program -> program != null)//.filter(Objects::nonNull)
                .collect(Collectors.toList());

        // add the new programs to the list of program states
        programStates.addAll(newProgramsList);

        // log after the execution
        programStates.forEach(program -> {
            try {
                repository.logProgramState(program);
                System.out.println(program);
            } catch (IOException e) {
                throw new FileException(e.getMessage());
            }
        });

        // save the states in the repo
        repository.setProgramsList(programStates);
    }

    public void executeAll() throws DivisionByZeroException, UnknownOperationException, IOException, InterruptedException {
        // create a new executor service
        this.executorService = Executors.newFixedThreadPool(2);

        // remove the completed programs
        List<ProgramState> programStatesList = this.removeCompletedPrograms(repository.getProgramsList());
        while(programStatesList.size() > 0){
            // execute once for all
            this.executeOnceAllPrograms(programStatesList);

            // remove completed ones
            programStatesList = removeCompletedPrograms(this.repository.getProgramsList());
        }

        executorService.shutdownNow();

        // update the repo
        repository.setProgramsList(programStatesList);

        /*
        ProgramState programState = repository.getCurrentProgramState();

        while(!programState.getExecutionStack().isEmpty()){

            try {
                executeOnce();

                programState.getHeap().setUnderlyingMap((HashMap<Integer, Integer>)
                        collectGarbage(programState.getSymbolTable().getUnderlyingMap().values(),
                                programState.getHeap().getUnderlyingMap()));
            }
            catch(DivisionByZeroException | UnknownOperationException | IOException | FileException | HeapException exception) {
                System.out.println(exception.getMessage());

                // close any open files, so no handles are left open after the
                // program terminates in case of an exception

                closeFileDescriptors(programState.getFileTable().getUnderlayingContainer());

                System.exit(0);
            }
        }
        */
        // clean the heap after finishing
        /*
        programState.getHeap().setUnderlyingMap((HashMap<Integer, Integer>)
                collectGarbage(programState.getSymbolTable().getUnderlyingMap().values(),
                        programState.getHeap().getUnderlyingMap()));
        */
    }

    public Map<Integer, Integer> collectGarbage(Collection<Integer> symbolTableValues, Map<Integer, Integer> heap){
        return heap.entrySet().stream().filter(e->symbolTableValues.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void closeFileDescriptors(Map<Integer, Model.FileTable.FileDescriptor> fileTableMap){
        fileTableMap.forEach((Integer key, Model.FileTable.FileDescriptor value) -> {
            try {
                value.getBufferedReader().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<ProgramState> removeCompletedPrograms(List<ProgramState> programStates){
        return programStates.stream()
                .filter(program -> (!program.isCompleted()))
                .collect(Collectors.toList());
    }

}




