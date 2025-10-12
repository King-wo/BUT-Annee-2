# ===============================
# SAE R3.08 - Partie 1.2 (Avec surbooking)
# Groupe 12 : (m1,p1)=(25,0.88), (m2,p2)=(105,0.93)
# b=100 ; t in {0, 0.5, 1} ; d in {200, 300}
# Produit :
#  - Table des n0 et E[R_n0] pour chaque (m,p,t,d)
#  - Graphes E[R_n] vs n pour chaque (m,p) avec une courbe par (t,d)
# ===============================

# --------- Paramètres globaux (énoncé) ---------
b  <- 100
Ts <- c(0, 0.5, 1)
Ds <- c(200, 300)

pairs_mp <- list(
  list(name="(m1,p1)=(25,0.88)", m=25,  p=0.88),
  list(name="(m2,p2)=(105,0.93)", m=105, p=0.93)
)

# --------- Fonctions utiles ---------
# E[max(0, X_n - m)] avec X_n ~ Binom(n,p)
E_max_binom <- function(n, m, p) {
  if (n <= m) return(0)
  i <- (m+1):n
  sum((i - m) * dbinom(i, n, p))
}

# E[R_n] = n b - t b n(1-p) - d E[max(0, X_n - m)]
ERn <- function(n, m, p, b, t, d) {
  n*b - t*b*n*(1-p) - d*E_max_binom(n, m, p)
}

# Recherche de n0 : n qui maximise E[R_n] sur une plage n = m..(m+K)
find_n0 <- function(m, p, b, t, d, K = 40) {
  n_vals <- m:(m+K)
  ER_vals <- vapply(n_vals, function(n) ERn(n, m, p, b, t, d), numeric(1))
  idx <- which.max(ER_vals)
  list(n0 = n_vals[idx], ERmax = ER_vals[idx], n_vals = n_vals, ER_vals = ER_vals)
}

# Arrondis conformes à l'énoncé
fmt_euro <- function(x) round(x, 0)                 # prix à l’euro
fmt_num  <- function(x) signif(x, 3)                # autres valeurs : 3 chiffres signif.

# --------- Calculs + Table récap ---------
res_rows <- list()

for (mp in pairs_mp) {
  m <- mp$m; p <- mp$p
  for (t in Ts) {
    for (d in Ds) {
      out <- find_n0(m, p, b, t, d, K = 40)
      res_rows[[length(res_rows)+1]] <- data.frame(
        Couple = mp$name, t = t, d = d,
        n0 = out$n0,
        ER_n0_euro = fmt_euro(out$ERmax),
        stringsAsFactors = FALSE
      )
    }
  }
}

res_table <- do.call(rbind, res_rows)
print(res_table, row.names = FALSE)

# --------- Graphes ---------
# Un graphe par couple (m,p), avec 6 courbes (t,d)
# Sauvegarde PNG dans le répertoire de travail
png_filename <- function(label) paste0("ERn_", gsub("[^0-9]", "", label), ".png")

for (mp in pairs_mp) {
  m <- mp$m; p <- mp$p
  # Créer canevas
  plot(NA, xlim = c(m, m+40), ylim = c(0, 1), type="n",
       xlab = "n (réservations acceptées)", ylab = "E[R_n] (€)",
       main = paste0("E[R_n] en fonction de n — ", mp$name))
  # On adapte l'axe Y après un premier passage pour calculer les bornes
  all_y <- c()
  curves <- list()
  for (t in Ts) for (d in Ds) {
    n_vals <- m:(m+40)
    ER_vals <- vapply(n_vals, function(n) ERn(n, m, p, b, t, d), numeric(1))
    curves[[paste0("t=",t,", d=",d)]] <- list(n=n_vals, y=ER_vals)
    all_y <- c(all_y, ER_vals)
  }
  # Fixer l'axe Y proprement et re-plot
  ylim <- range(all_y)
  plot(NA, xlim = c(m, m+40), ylim = ylim, type="n",
       xlab = "n (réservations acceptées)", ylab = "E[R_n] (€)",
       main = paste0("E[R_n] en fonction de n — ", mp$name))
  # Dessiner chaque courbe
  cols <- c("black","blue","red","darkgreen","purple","orange")
  k <- 1
  leg <- c()
  for (nm in names(curves)) {
    lines(curves[[nm]]$n, curves[[nm]]$y, type="b", pch=16, col=cols[k])
    leg <- c(leg, nm)
    k <- k + 1
  }
  legend("bottomright", legend = leg, lty = 1, pch = 16,
         col = cols[seq_along(leg)], cex = 0.9, bg = "white")
  # Sauvegarde PNG
  fname <- png_filename(mp$name)
  dev.copy(png, filename = fname, width = 1200, height = 800, res = 150)
  dev.off()
  message("Figure enregistrée : ", fname)
}

# --------- Export table (CSV) ---------
write.csv(res_table, file = "table_n0_ERn0_g12.csv", row.names = FALSE)
message("Table exportée : table_n0_ERn0_g12.csv")

# Notes :
# - Les valeurs exactes dépendront de la granularité (K) et des (t,d).
# - Tu peux augmenter K si besoin (m+60, m+80) si les courbes montent encore.
