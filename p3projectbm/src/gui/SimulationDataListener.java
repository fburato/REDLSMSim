package gui;

public interface SimulationDataListener {
  public void sleepTimeEventPerformed(ButtonPanel b);
  public void nodeDimEventPerformed(ButtonPanel b);
  public void crossDimEventPerformed(ButtonPanel b);
  public void startEventPerformed(ButtonPanel b);
  public void stopEventPerformed(ButtonPanel b);
  public void pauseSimEventPerformed(ButtonPanel b);
  public void pauseTestEventPerformed(ButtonPanel b);
  public void visibleArchEventPerformed(ButtonPanel b);
  
}
