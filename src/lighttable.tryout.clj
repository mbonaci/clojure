;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.

(take 9 (cycle [1 2 3 4]))

(reverse "woolf")
(apply str (reverse "woolf"))
(str (reverse "woolf"))

(doto (new javax.swing.JFrame) (.add (javax.swing.JLabel. "Hello")) .pack .show)

(type javax.swing.JFrame)
