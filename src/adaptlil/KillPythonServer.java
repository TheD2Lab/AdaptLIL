package adaptlil;

import adaptlil.http.PythonServerCore;

public class KillPythonServer extends Thread {

    private PythonServerCore pythonServerCore;

    public KillPythonServer(PythonServerCore pythonServerCore) {
        this.pythonServerCore = pythonServerCore;
    }

    @Override
    public void run() {
        pythonServerCore.closeServer();
    }
}
