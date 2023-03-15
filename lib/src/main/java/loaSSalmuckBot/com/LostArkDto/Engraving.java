package loaSSalmuckBot.com.LostArkDto;

import lombok.Getter;

@Getter
public enum Engraving {
//	adrenaline("아드레날린",new float[]{ 1.8f,3.6f,6f}),
	allOutAttack("속전속결",new float[]{ 0f,0f,0f}),
	awakening("각성",new float[]{ 0f,0f,0f}),
	necromancy("강령술",new float[]{ 0f,0f,0f}),
	enhancedShield("강화 방패",new float[]{ 0f,0f,0f}),
	masterBrawler("결투의 대가",new float[]{ 0f,0f,0f}),
	dropsOfEther("구슬동자",new float[]{ 0f,0f,0f}),
	strongWill("굳은 의지",new float[]{ 0f,0f,0f}),
	vitalPointStrike("급소 타격",new float[]{ 0f,0f,0f}),
	masterOfAmbush("기습의 대가",new float[]{ 0f,0f,0f}),
	emergencyRescue("긴급 구조",new float[]{ 0f,0f,0f}),
	masterTenacity("달인의 저력",new float[]{ 0f,0f,0f}),
	raidCaptain("돌격 대장",new float[]{ 0f,0f,0f}),
	magickStream("마나의 흐름",new float[]{ 0f,0f,0f}),
	barricade("바리케이트",new float[]{ 0f,0f,0f}),
	lightningFury("번개의 분노",new float[]{ 0f,0f,0f}),
	brokenBone("부러진 뼈",new float[]{ 0f,0f,0f}),
	crushingFist("분쇄의 주먹",new float[]{ 0f,0f,0f}),
	fortitude("불굴",new float[]{ 0f,0f,0f}),
	preemptiveStrike("선수필승",new float[]{ 0f,0f,0f}),
	superCharge("슈퍼 차지",new float[]{ 0f,0f,0f}),
	contender("승부사",new float[]{ 0f,0f,0f}),
	sightFocus("시선 집중",new float[]{ 0f,0f,0f}),
	shieldPiercing("실드 관통",new float[]{ 0f,0f,0f}),
	stabilisedStatus("안정된 상태",new float[]{ 0f,0f,0f}),
	disrespect("약자 무시",new float[]{ 0f,0f,0f}),
	divineProtection("여신의 가호",new float[]{ 0f,0f,0f}),
//	etherPredator("에테르 포식자",new float[]{ 6f,9f,15f}),
	keenBluntWeapon("예리한 둔기",new float[]{0f,0f,0f}),
	Grudge("원한",new float[]{ 0f,0f,0f}),
	cursedDoll("저주받은 인형",new float[]{ 3f,8f,16f}),
	Expert("전문의",new float[]{ 0f,0f,0f}),
	spiritAbsorption("정기 흡수",new float[]{ 0f,0f,0f}),
	preciseDagger("정밀 단도",new float[]{ 0f,0f,0f}),
	heavyArmor("중갑 착용",new float[]{ 0f,0f,0f}),
	increaseMass("질량 증가",new float[]{ 4f,10f,18f}),
	maxMPIncrease("최대 마나 증가",new float[]{ 0f,0f,0f}),
	propulsion("추진력",new float[]{ 0f,0f,0f}),
	hitMaster("타격의 대가",new float[]{ 0f,0f,0f}),
	masterofEscape("탈출의 명수",new float[]{ 0f,0f,0f}),
	explosiveExpert("중갑 착용",new float[]{ 0f,0f,0f}),
	attackPowerDecrease("공격력 감소",new float[]{ -2f,-4f,-6f}),
	attackSpeedDecrease("공격속도 감소",new float[]{ 0f,0f,0f}),
	defenseDecrease("방어력 감소",new float[]{ 0f,0f,0f}),
	speedDecrease("이동속도 감소",new float[]{ 0f,0f,0f}),
	none("직업각인",new float[]{ 0f,0f,0f});
	
	
	private final String name;
	private final float[] level;
	

	Engraving(String name, float[] level) {
		this.name = name;
		this.level = level;
		// TODO Auto-generated constructor stub
	}
	public static Engraving getEngravingByName(String engravingName) {
        for (Engraving engraving : Engraving.values()) {
            if (engraving.getName().equals(engravingName)) {
                return engraving;
            }
        }
        return none;
    }
}
