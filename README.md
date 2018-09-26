# keycloak-registration-userlist

This keycloak plugin needs a system variable called EMAIL_WHITE_LIST with a path to a text file. 
This path is used to read the file with the allowed user e-mails addresses.

For example: /Users/test/documents/file.txt

The file has to be a plain-text file which contains an e-mail address per line:

aille@web.de

bwa@adorsys.de

aro@adorsys.de

