package adaptovis;

public class KillPidThread extends Thread {

    private long pid;

    public KillPidThread(long pid) {
        this.pid = pid;
    }

    @Override
    public void run() {
        //List<String> killArgs = null;
        //if (SystemUtils.IS_OS_LINUX) {
        //    killArgs = Arrays.stream(new String[] {"kill", ""+pid}).toList();
        //} else if (SystemUtils.IS_OS_WINDOWS) {
        //    killArgs = Arrays.stream(new String[] {"taskkill", "/PID", ""+pid, "/t", "/f"}).toList();
        //} else if (SystemUtils.IS_OS_MAC_OSX) {
        //    killArgs = Arrays.stream(new String[] {"kill", "-9", ""+pid}).toList();
        //}
        //
        //ProcessBuilder processBuilder = new ProcessBuilder(killArgs);
        //processBuilder.redirectErrorStream(true);
        //try {
        //    Process p = processBuilder.start();
        //    ServerMain.readProcessOutput(p, true);
        //} catch (IOException e) {
        //    //Could not end via PID
        //    System.err.println("Could not kill PID: " + pid + " (python kerasServer)");
        //    throw new RuntimeException(e);
        //}
    }
}
