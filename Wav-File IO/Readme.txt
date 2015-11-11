TODOs:

Mustererkennung: 
	1. die Berechnung der Linien-Polygone kommentieren und dokumentieren!
	2. die Polygon-Größe für die Höhe und Breite getrennt definieren können
	3. neue Muster automatisiert finden (= jede mögliche Kombination von Samples als Muster versuchen - wenn es öfter als 3 mal gefunden wird, gilt eine Kombination als Muster)
	
Verbesserungsvorschläge:
	1. fast Peeks (praktisch Sattelpunkt) ausfindig machen und sie bei der Muster suche mit beachten
	
	
Informationen
	1. 	Derzeit ist es lediglich möglich WAV-Dateien mit maximal 2147483647.
		Dies entspricht bei einer Frame-Rate von 44100 ca. 13,5 Stunden Laufzeit.
		Diese Einschränkung besteht, da die einzelnen Samples in einem Array abgelegt werden und ein Array maximal 2147483647(= größte int-Zahl) Elemente enthalten kann.
	2.	Derzeit kann das Tool nur den ersten Channel einer WAV-Datei analysieren. Alle weiteren Channels einer WAV-Datei werden ignoriert!