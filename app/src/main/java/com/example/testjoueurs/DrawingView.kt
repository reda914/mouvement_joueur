package com.example.testjoueurs

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

// Cette classe représente une vue personnalisée pour dessiner et déplacer une boule bleue sur l'écran.
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // On met tout ça =O ou null pour les INITIALISER ; déjà préciser leur type et qu'elles sont private
    //idée: possibilité de les mettre en lateinit

    // Le PINCEAU utilisé pour DESSINER la boule bleue.
    // Le ? rend la variable nullable
    private var mPaintBleu: Paint? = null
    private var mPaintVert: Paint? = null

    // Le RECTANGLE représentant la ZONE de la boule.
    private var mBallRectB: RectF? = null
    private var mBallRectV: RectF? = null

    // Variables pour stocker la DERNIERE POSITION du toucher
    private var mLastTouchX: Float = 0.toFloat()
    private var mLastTouchY: Float = 0.toFloat()

    // Indique si l'utilisateur a cliqué sur la balle.
    // False = il a pas cliqué ; True = il a cliqué ;
    private var a_cliquer: Boolean = false
    private var a_cliquerV: Boolean = false

    // La hauteur de l'écran
    // Conversion en float car consomme moins de mémoire ; comparé à double (64bits), float(32bits) est moins précis
    // Besoin de passer à Double si on veut être plus précis
    private var screenHeight: Float = 0.toFloat()
    private var screenWidth: Float = 0.toFloat()

    val ballSize = 150 // Diamètre de la boule


    // sert à lancer l'Initialisation, appelée lors de la création de l'instance de la vue.
    init {
        setupDrawing()
    }

    // Méthode pour configurer le dessin initial de la boule.
    private fun setupDrawing() {
        // Initialisation du PINCEAU.
        mPaintBleu = Paint()
        mPaintVert = Paint()
        // Configuration de la COULEUR du pinceau en bleu.
        mPaintBleu!!.color = Color.BLUE
        mPaintVert!!.color = Color.GREEN
        // Activation de LISSAGE (anti-alisaing) pour des bords plus lisses.
        mPaintBleu!!.isAntiAlias = true
        mPaintVert!!.isAntiAlias = true
        // Configuration du style de dessin du pinceau à REMPLISSAGE.
        mPaintBleu!!.style = Paint.Style.FILL
        mPaintVert!!.style = Paint.Style.FILL
        // Initialisation du rectangle représentant la boule.

        val h = resources.displayMetrics.heightPixels.toFloat()
        val w = resources.displayMetrics.widthPixels.toFloat()

        val rectXB = w / 2 //  coordonnée de départ x
        val rectYB = h / 4 //  coordonnée de départ y
        val rectXV = w / 2 //  coordonnée de départ x
        val rectYV = 3*h / 4 //  coordonnée de départ y
        mBallRectB = RectF(rectXB, rectYB, rectXB + ballSize, rectYB + ballSize)
        mBallRectV = RectF(rectXV, rectYV, rectXV + ballSize, rectYV + ballSize)

        // Récupération de la hauteur et largeur de l'écran
        screenHeight = resources.displayMetrics.heightPixels.toFloat()-300 //Le -300 est un traficotage pour que ça colle avec les lignes de terrain
        screenWidth = resources.displayMetrics.widthPixels.toFloat()-150 //Traficotage également
        // ATTENTION: RISQUE DU TRAFICOTAGE EST QUE CA NEST VALABLE QUE SUR UN APPAREIL??
    }

    // Méthode appelée pour dessiner la vue.
    // Redéfinition de la méthode pour dessiner la boule
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Dessin de la boule sur le canvas avec le pinceau configuré.
        canvas.drawOval(mBallRectB!!, mPaintBleu!!)
        canvas.drawOval(mBallRectV!!, mPaintVert!!)
    }

    // Méthode pour mettre à jour la position de la boule en fonction des coordonnées x et y fournies.
    private fun updateBallBPosition(x: Float, y: Float) {
        // Vérifie si la nouvelle position est dans la moitié supérieure de l'écran
        // Si oui, alors on change les attributs=position de mBallRect qui est le rectangle où est inscrit la balleS
        if (y >= 12 && y <= screenHeight / 2 && x >= 12 && x <= screenWidth) {
            mBallRectB!!.left = x
            mBallRectB!!.top = y
            mBallRectB!!.right = x + ballSize
            mBallRectB!!.bottom = y + ballSize
        }
    }

    private fun updateBallVPosition(x: Float, y: Float) {
        // Vérifie si la nouvelle position est dans la moitié supérieure de l'écran
        // Si oui, alors on change les attributs=position de mBallRect qui est le rectangle où est inscrit la balleS
        if (y < screenHeight+150 && y > screenHeight / 2 +150 && x >= 12 && x <= screenWidth) {
            mBallRectV!!.left = x
            mBallRectV!!.top = y
            mBallRectV!!.right = x + ballSize
            mBallRectV!!.bottom = y + ballSize
        }
    }



    // Redéfinition de onTouchEvent
    // Méthode appelée lorsqu'un événement tactile est détecté sur la vue.
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Récupération des coordonnées x et y de l'événement tactile.
        val x = event.x
        val y = event.y

        // Gestion des différents types d'actions tactiles.
        // ACTION_DOWN veut dire doigt DOWN sur l'écran
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Lorsque l'utilisateur appuie sur l'écran,
                // vérifie si la position de l'événement est à l'intérieur de la boule.
                if (mBallRectB!!.contains(x, y)) {
                    // Indique que l'utilisateur a cliqué sur la balle.
                    a_cliquer = true
                }
                if (mBallRectV!!.contains(x, y)) {
                    // Indique que l'utilisateur a cliqué sur la balle.
                    a_cliquerV = true
                }
                // Stocke la position du toucher actuel pour la prochaine mise à jour.
                mLastTouchX = x
                mLastTouchY = y
            }
            // ACTION_MOVE veut dire doigt MOVE sur l'écran
            MotionEvent.ACTION_MOVE -> {
                // Vérifie si l'utilisateur a cliqué sur la balle avant de commencer à déplacer.
                if (a_cliquer) {
                    // Calcul de la différence de position entre le toucher actuel et le toucher précédent.
                    val diffx = x - mLastTouchX
                    val diffy = y - mLastTouchY
                    // Met à jour la position de la boule en ajoutant la différence de position à sa position actuelle.
                    updateBallBPosition(mBallRectB!!.left + diffx, mBallRectB!!.top + diffy)
                    // Stocke la position du toucher actuel pour la prochaine mise à jour.
                    mLastTouchX = x
                    mLastTouchY = y
                    // Demande à la vue de se redessiner pour afficher la nouvelle position de la boule.
                    invalidate()
                }
                if (a_cliquerV) {
                    // Calcul de la différence de position entre le toucher actuel et le toucher précédent.
                    val diffx = x - mLastTouchX
                    val diffy = y - mLastTouchY
                    // Met à jour la position de la boule en ajoutant la différence de position à sa position actuelle.
                    updateBallVPosition(mBallRectV!!.left + diffx, mBallRectV!!.top + diffy)
                    // Stocke la position du toucher actuel pour la prochaine mise à jour.
                    mLastTouchX = x
                    mLastTouchY = y
                    // Demande à la vue de se redessiner pour afficher la nouvelle position de la boule.
                    invalidate()
                }
            }
            //ACTION_UP veut dire doigts pas sur sur l'écran
            MotionEvent.ACTION_UP -> {
                // Réinitialise l'état de glissement lorsque l'utilisateur relâche l'écran.
                a_cliquer = false
                a_cliquerV = false
            }
        }

        // Indique que l'événement a été traité.
        return true
    }
}