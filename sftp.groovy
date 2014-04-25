import com.jcraft.jsch.*
import java.io.IOException

JSch jsch               = null
Session sftpSession     = null
Channel channel         = null
ChannelSftp sftpChannel = null

String host             = "sftp.example.com"
String username         = "mysftpuser"
String privateKeyFile   = "/path/to/my/private.key"
String inputFile        = "/file/to/be.transferred"
String remoteFile       = "remote.filename"

println "SFTP: Connecting to host ${sftpSession.host}..."

try {
    jsch        = new JSch()
    sftpSession = jsch.getSession(username, host, 22)
    sftpSession.setConfig("StrictHostKeyChecking", "no")
    jsch.addIdentity(privateKeyFile)
    sftpSession.connect()
    channel     = sftpSession.openChannel("sftp")
    channel.connect()
    sftpChannel = channel as ChannelSftp
    println "SFTP: Connected to host ${sftpSession.host}."
} catch (IOException e) {
    println "Unexpected error during connect to SFTP-Server."
    e.printStackTrace
}

try {
    println "SFTP: Transferring local file '${inputFile}' to remote file '${remoteFile}'..."
    sftpChannel.put(inputFile, remoteFile)
    println "SFTP: Transferred local file '${inputFile}' to remote file '${remoteFile}'."
} catch (IOException e) {
    println "Unexpected error during file transfer to SFTP-Server."
    e.printStackTrace
}
println "SFTP: Disconnecting from host ${sftpSession.host}..."

try {
    sftpChannel.disconnect()
    sftpSession.disconnect()
} catch (Exception e) {
    println "SFTP Connection could not be closed properly."
    e.printStackTrace
}

println "SFTP: Disconnected from host ${sftpSession.host}."

