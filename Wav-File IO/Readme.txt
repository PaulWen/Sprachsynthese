TODOs:

BUGS: wenn ich ganz oft das gleiche Muster hintereinander packe und dann nach dem Muster suche, dann findet er es nicht mehr


Mustererkennung: 
	1. die Berechnung der Linien-Polygone kommentieren und dokumentieren!
	2. Muster erkennen und wiederfinden (mittels einem Bewertungsverfahren) 
	
	
Verbesserungsvorschläge:
	1. fast Peeks (praktisch Sattelpunkt) ausfindig machen und sie bei der Muster suche mit beachten
	
	
Informationen
	1. 	Derzeit ist es lediglich möglich WAV-Dateien mit maximal 2147483647.
		Dies entspricht bei einer Frame-Rate von 44100 ca. 13,5 Stunden Laufzeit.
		Diese Einschränkung besteht, da die einzelnen Samples in einem Array abgelegt werden und ein Array maximal 2147483647(= größte int-Zahl) Elemente enthalten kann.