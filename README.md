# Vanilla Java Application

This application does work such as editing XML files and sending reports a database.

## How  to run

It is run from the Runner.java class.

The RunnerWithoutXPath.java class will give same result, but the solution is full of code smells.

## Notes

Processing is done every 30 seconds with the use of the Timer and TimerTask classes. For faster use, you can adjust the
period from 30000 milis to 1000, the result will be the same.

The in and out directories can be found in the telekomdocuments folder.

example1.xml, example2.xml, and example3.xml are the same file, they are just there to showcase that multiple files are
read from a directory.

## Testing

Testing was done for JSON parsing and for XML editing.