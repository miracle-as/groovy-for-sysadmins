import com.jcraft.jsch.*
import java.io.IOException
import SFTPException

class Stfp {

    def transferToSftp(String host, String username, String privateKeyFile, String inputFile, String remoteFile) {

        JSch jsch               = null
        Session sftpSession     = null
        Channel channel         = null
        ChannelSftp sftpChannel = null

        try {
            try {
                jsch        = new JSch()
                sftpSession = jsch.getSession(username, host, 22)
                sftpSession.setConfig("StrictHostKeyChecking", "no")
                jsch.addIdentity(privateKeyFile)
                log.info "SFTP: Connecting to host ${sftpSession.host}..."
                sftpSession.connect()
                channel     = sftpSession.openChannel("sftp")
                channel.connect()
                sftpChannel = channel as ChannelSftp
                log.info "SFTP: Connected to host ${sftpSession.host}."
            } catch (IOException e) {
                throw new SFTPException("Unexpected error during connect to SFTP-Server.", e)
            }

            try {
                log.info "SFTP: Transferring local file '${inputFile}' to remote file '${remoteFile}'..."
                sftpChannel.put(inputFile, remoteFile)
                log.info "SFTP: Transferred local file '${inputFile}' to remote file '${remoteFile}'."
            } catch (IOException e) {
                throw new SFTPException("Unexpected error during file transfer to SFTP-Server.", e)
            }
        } catch (SFTPException e) {
            log.error(e.getMessage())
        } finally {
            log.info "SFTP: Disconnecting from host ${sftpSession.host}..."

            try {
                sftpChannel.disconnect()
            } catch (Exception e) {
                log.warn("Channel for SFTP Connection could not be closed properly.", e)
            }

            try {
                sftpSession.disconnect()
            } catch (Exception e) {
                log.warn("Session for SFTP Connection could not be closed properly.", e)
            }

            log.info "SFTP: Disconnected from host ${sftpSession.host}."
        }
    }
}
