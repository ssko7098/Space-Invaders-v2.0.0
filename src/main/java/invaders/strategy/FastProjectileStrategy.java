package invaders.strategy;

import invaders.factory.Projectile;

public class FastProjectileStrategy implements ProjectileStrategy{

    @Override
    public void update(Projectile p) {
        double newYPos = p.getPosition().getY() + 3;
        p.getPosition().setY(newYPos);
    }

    @Override
    public String getStrategy() {
        return "fast_straight";
    }
}
